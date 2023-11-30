package com.sky.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.OrderMQInfo;
import com.sky.entity.Orders;
import com.sky.exception.RepeatException;
import com.sky.mapper.OrdersMapper;
import com.sky.result.PageQuery;
import com.sky.service.OrdersService;
import com.sky.service.ProductService;
import com.sky.utils.JwtUtils;
import com.sky.utils.RedisCache;
import com.sky.vo.OrdersVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper , Orders> implements OrdersService {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查看所有订单
     * @return
     */
    @Override
    public PageDTO<OrdersVO> selectAll(PageQuery query) {
        Page<Orders> page = query.toMpPageDefaultSortByCreateTimeDesc();
        page(page);
        return PageDTO.of(page, OrdersVO.class);
    }

    /**
     * 按id查询
     * @param id
     * @return
     */
    @Override
    public OrdersVO selectByID(Long id) {
        Orders orders = baseMapper.selectById(id);
        OrdersVO ordersVO = new OrdersVO();
        BeanUtils.copyProperties(orders,ordersVO);
        return ordersVO;
    }

    /**
     * 用户下单
     * @param ordersDTO
     * @param token
     * @return
     */
    @Transactional
    @Override
    public OrdersVO placeNewOrder(OrdersDTO ordersDTO, String token) {

        //创建订单号：日期+用户id+下单商品id
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        int dayOfMonth = date.getDayOfMonth();
        String date1 = "" + year + month + dayOfMonth;
        String userId = JwtUtils.getUserId(token);
        Long[] ids = ordersDTO.getIds();
        String id1 = "";
        StringBuilder sb = new StringBuilder(id1);
        for (Long id : ids) {
            sb.append(id);
        }
        id1 = sb.toString();
        String number = "OD-" + userId + "CS" + date1 + id1;
        String rnumber = "ROD-" + userId + "CS" + date1 + id1;

        List<String> redisKey = new ArrayList<>();
        redisKey.add(number);
        redisKey.add(rnumber);

        //从Redis缓存中取出订单查询是否重复下单
        Object val = redisCache.getCacheObject(number);
        if (!Objects.isNull(val)){
            throw new RepeatException(MessageConstant.ORDER_HAS_BEEN_CREATE);
        } else {
            //再从数据库中查询是否重复下单
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders::getNumber, number);
            if (!baseMapper.selectList(wrapper).isEmpty()){
                throw new RepeatException(MessageConstant.ORDER_HAS_BEEN_CREATE);
            }
        }

        //TODO e
        //将该商品库存 - 1,顺便判断库存余量
        //productService.productGoodsMinus(ids);

        //计算订单总支付金额
        AtomicInteger totalPrice = productService.countTotalPriceByIDs(ids);

        //创建Orders对象，写入数据库
        Map<String, Object> info = JwtUtils.getInfo(token);
        String userName = null;
        if (info != null) {
            userName = (String) info.get("userName");
        }
        Orders orders = Orders.builder()
                .number(number)
                .status(Orders.UN_PAID)
                .userId(Long.valueOf(userId))
                .orderTime(LocalDateTime.now())
                .payStatus(Orders.UN_PAID)
                .amount(BigDecimal.valueOf(totalPrice.get()))
                .remark(ordersDTO.getRemark())
                .userName(userName)
                .phone(ordersDTO.getPhone())
                .address(ordersDTO.getAddress())
                .consignee(ordersDTO.getConsignee()).build();
        baseMapper.insert(orders);

        //TODO 存入Redis缓存，持续时间为30min
        //TODO 连存两条Redis数据，第二条比第一条存在时间长5秒，供监听Redis过期时使用
//        redisCache.setCacheLong(number,ids);
//        redisCache.expire(number,1800);
//        redisCache.setCacheLong(rnumber,ids);
//        redisCache.expire(rnumber,1805);

        //发布mq通知，判断库存余量后减少商品库存，并将订单存入redis缓存持续30min
        OrderMQInfo orderMQInfo = OrderMQInfo.builder()
                        .arr(redisKey)
                        .ids(ids)
                        .build();
        String orderMQInfoToJSON = JSONUtil.toJsonStr(orderMQInfo);
        rabbitTemplate.convertAndSend("place.topic", "place.success", orderMQInfoToJSON);

        OrdersVO ordersVO = new OrdersVO();
        BeanUtils.copyProperties(orders,ordersVO);
        return ordersVO;
    }

    @Override
    public void setOrderNumberRedisCache(List<String> redisKey, Long[] ids) {
        redisCache.setCacheLong(redisKey.get(0), ids);
        redisCache.expire(redisKey.get(0),1800);
        redisCache.setCacheLong(redisKey.get(1), ids);
        redisCache.expire(redisKey.get(1),1805);
    }


    /**
     * 用户支付
     * @param number
     * @return
     */
    @Transactional
    @Override
    public int payOrder(String number) {
        //判断是否重复支付
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getPayStatus, Orders.PAID);
        if (!baseMapper.selectList(wrapper).isEmpty()) {
            throw new RepeatException(MessageConstant.HAS_BEEN_PAID);
        }

        //删除Redis中的缓存
        redisCache.deleteObject(number);

        //更新数据库
        LambdaUpdateWrapper<Orders> wrapper1 = new LambdaUpdateWrapper<>();
        wrapper1.eq(Orders::getNumber, number)
                .set(Orders::getPayStatus,Orders.PAID)
                .set(Orders::getStatus, Orders.COMPLETED)
                .set(Orders::getCheckoutTime, LocalDateTime.now());
        return baseMapper.update(null, wrapper1);
        
    }

    /**
     * 用户取消订单
     * @param ordersCancelDTO
     * @return
     */
    @Transactional
    @Override
    public int cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        //TODO 还原库存
        Long[] cacheLong = redisCache.getCacheLong(ordersCancelDTO.getNumber());
        log.error(Arrays.toString(cacheLong));
        productService.productGoodsRecovery(cacheLong);

        //删除Redis中的缓存
        redisCache.deleteObject(ordersCancelDTO.getNumber());

        //更新订单状态
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getNumber, ordersCancelDTO.getNumber())
                .set(Orders::getPayStatus,Orders.UN_PAID)
                .set(Orders::getStatus, Orders.CANCELLED)
                .set(Orders::getCancelTime, LocalDateTime.now())
                .set(Orders::getCancelReason, ordersCancelDTO.getCancelReason());
        return baseMapper.update(null, wrapper);

    }

    /**
     * 过期超时取消订单，此被RedisListner调用
     * @param number
     */
    @Transactional
    @Override
    public void timeOutCancelOrder(String number) {
        //TODO 此次取出克隆的Redis数据
        String[] split = number.split("-");
        Long[] cacheLong = redisCache.getCacheLong("ROD"+"-"+split[1]);
        productService.productGoodsRecovery(cacheLong);

        //更新订单状态
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getNumber, number)
                .set(Orders::getPayStatus, Orders.UN_PAID)
                .set(Orders::getStatus, Orders.CANCELLED)
                .set(Orders::getCancelTime, LocalDateTime.now())
                .set(Orders::getCancelReason, "订单超时");
        baseMapper.update(null, wrapper);
    }


}
