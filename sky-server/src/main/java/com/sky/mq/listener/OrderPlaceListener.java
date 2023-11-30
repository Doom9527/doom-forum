package com.sky.mq.listener;

import cn.hutool.json.JSONUtil;
import com.sky.constant.RabbitMQConstant;
import com.sky.entity.OrderPlaceMQInfo;
import com.sky.service.OrdersService;
import com.sky.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPlaceListener {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductService productService;

    /**
     * 下单后库存减少
     * @param payload
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConstant.ORDER_PLACE_QUEUE1, durable = RabbitMQConstant.DURABLE_TRUE),
            exchange = @Exchange(name = RabbitMQConstant.ORDER_PLACE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConstant.ORDER_PLACE_KEY
    ))
    public void listenOrderPlaceSuccess1(@Payload String payload){
        OrderPlaceMQInfo orderPlaceMQInfo = getOrderPlaceMQInfo(payload);
        Long[] ids = orderPlaceMQInfo.getIds();
        productService.productGoodsMinus(ids);
        log.error("库存减少成功: " + Arrays.toString(ids));
    }

    /**
     * 下单后添加redis缓存
     * @param payload
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConstant.ORDER_PLACE_QUEUE2, durable = RabbitMQConstant.DURABLE_TRUE),
            exchange = @Exchange(name = RabbitMQConstant.ORDER_PLACE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConstant.ORDER_PLACE_KEY
    ))
    public void listenOrderPlaceSuccess2(@Payload String payload){
        OrderPlaceMQInfo orderPlaceMQInfo = getOrderPlaceMQInfo(payload);
        Long[] ids = orderPlaceMQInfo.getIds();
        List<String> redisKey = orderPlaceMQInfo.getArr();
        ordersService.setOrderNumberRedisCache(redisKey, ids);
        log.error("添加redis缓存成功: " + redisKey.toString());
    }

    private static OrderPlaceMQInfo getOrderPlaceMQInfo(String payload) {
        OrderPlaceMQInfo orderPlaceMQInfo = JSONUtil.toBean(payload, OrderPlaceMQInfo.class);
        return orderPlaceMQInfo;
    }

}