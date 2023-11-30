package com.sky.mq.listener;

import cn.hutool.json.JSONUtil;
import com.sky.entity.OrderMQInfo;
import com.sky.service.OrdersService;
import com.sky.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPlaceListener {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductService productService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "mark.order.place.queue", durable = "true"),
            exchange = @Exchange(name = "place.topic", type = ExchangeTypes.TOPIC),
            key = "place.success"
    ))
    public void listenOrderPlaceSuccess1(@Payload String payload){
        OrderMQInfo orderMQInfo = getOrderMQInfo(payload);
        Long[] ids = orderMQInfo.getIds();
        productService.productGoodsMinus(ids);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "mark.order.place.queue", durable = "true"),
            exchange = @Exchange(name = "place.topic", type = ExchangeTypes.TOPIC),
            key = "place.success"
    ))
    public void listenOrderPlaceSuccess2(@Payload String payload){
        OrderMQInfo orderMQInfo = getOrderMQInfo(payload);
        Long[] ids = orderMQInfo.getIds();
        List<String> redisKey = orderMQInfo.getArr();
        ordersService.setOrderNumberRedisCache(redisKey, ids);
    }

    private static OrderMQInfo getOrderMQInfo(String payload) {
        OrderMQInfo orderMQInfo = JSONUtil.toBean(payload, OrderMQInfo.class);
        return orderMQInfo;
    }

}