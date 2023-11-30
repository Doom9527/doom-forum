package com.sky.constant;

/**
 * RabbitMQ消息类
 */
public class RabbitMQConstant {
    /**
     * durable
     */
    public static final String DURABLE_TRUE = "true";

    /**
     * 下单成功队列
     */
    public static final String ORDER_PLACE_QUEUE =  "mark.order.place.queue";

    /**
     * 下单成功交换机
     */
    public static final String ORDER_PLACE_EXCHANGE = "place.topic";

    /**
     * 下单成功KEY
     */
    public static final String ORDER_PLACE_KEY = "place.success";

    /**
     * 支付成功队列
     */
    public static final String ORDER_PAY_QUEUE =  "mark.order.pay.queue";

    /**
     * 支付成功交换机
     */
    public static final String ORDER_PAY_EXCHANGE = "pay.topic";

    /**
     * 支付成功KEY
     */
    public static final String ORDER_PAY_KEY = "pay.success";
}
