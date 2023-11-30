package com.sky.mq.listener;

import cn.hutool.json.JSONUtil;
import com.sky.constant.RabbitMQConstant;
import com.sky.entity.OrderCanceMQInfo;
import com.sky.entity.OrderPayMQInfo;
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
public class OrderCancelListener {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductService productService;

    /**
     * 订单取消删除redis缓存
     * @param payload
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConstant.ORDER_CANCEL_MANUAL_QUEUE, durable = RabbitMQConstant.DURABLE_TRUE),
            exchange = @Exchange(name = RabbitMQConstant.ORDER_CANCEL_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConstant.ORDER_CANCEL_MANUAL_KEY
    ))
    public void listenOrderManualCancel(@Payload String payload){
        OrderCanceMQInfo orderCancelMQInfo = getOrderCancelMQInfo(payload);
        List<String> redisKey = orderCancelMQInfo.getRedisKey();
        ordersService.deleteOrderNumberRedisCache(redisKey);
        log.error("删除redis缓存成功: " + redisKey.toString());
    }

    /**
     * 还原库存
     * @param payload
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConstant.ORDER_CANCEL_QUEUE, durable = RabbitMQConstant.DURABLE_TRUE),
            exchange = @Exchange(name = RabbitMQConstant.ORDER_CANCEL_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConstant.ORDER_CANCEL_KEY
    ))
    public void listenOrderCancel(@Payload String payload){
        OrderCanceMQInfo orderCancelMQInfo = getOrderCancelMQInfo(payload);
        Long[] ids = orderCancelMQInfo.getIds();
        productService.productGoodsRecovery(ids);
        log.error("还原库存成功: " + Arrays.toString(ids));
    }

    private static OrderCanceMQInfo getOrderCancelMQInfo(String payload) {
        OrderCanceMQInfo orderCanceMQInfo = JSONUtil.toBean(payload, OrderCanceMQInfo.class);
        return orderCanceMQInfo;
    }
    
}
