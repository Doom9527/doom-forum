package com.sky.mq.listener;

import cn.hutool.json.JSONUtil;
import com.sky.constant.RabbitMQConstant;
import com.sky.entity.OrderPayMQInfo;
import com.sky.entity.OrderPlaceMQInfo;
import com.sky.service.OrdersService;
import com.sky.utils.RedisCache;
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

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPayListener {

    @Autowired
    private OrdersService ordersService;

    /**
     * 支付成功删除redis缓存
     * @param payload
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConstant.ORDER_PAY_QUEUE, durable = RabbitMQConstant.DURABLE_TRUE),
            exchange = @Exchange(name = RabbitMQConstant.ORDER_PAY_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConstant.ORDER_PAY_KEY
    ))
    public void listenOrderPlaySuccess(@Payload String payload){
        OrderPayMQInfo orderPayMQInfo = getOrderPayMQInfo(payload);
        List<String> redisKey = orderPayMQInfo.getRedisKey();
        ordersService.deleteOrderNumberRedisCache(redisKey);
        log.error("删除redis缓存成功: " + redisKey.toString());
    }

    private static OrderPayMQInfo getOrderPayMQInfo(String payload) {
        OrderPayMQInfo orderPayMQInfo = JSONUtil.toBean(payload, OrderPayMQInfo.class);
        return orderPayMQInfo;
    }
}
