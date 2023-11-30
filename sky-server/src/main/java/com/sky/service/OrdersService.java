package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.Orders;
import com.sky.result.PageQuery;
import com.sky.vo.OrdersVO;

import java.util.List;

public interface OrdersService extends IService<Orders> {
    PageDTO<OrdersVO> selectAll(PageQuery query);

    OrdersVO selectByID(Long id);

    OrdersVO placeNewOrder(OrdersDTO ordersDTO, String token);

    void setOrderNumberRedisCache (List<String> redisKey, Long[] ids);

    int payOrder(String number);

    int cancelOrder(OrdersCancelDTO ordersCancelDTO);

    void timeOutCancelOrder(String number);
}
