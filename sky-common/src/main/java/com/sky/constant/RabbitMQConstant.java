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
     * 下单成功队列1
     */
    public static final String ORDER_PLACE_QUEUE1 =  "mark.order.place.queue1";

    /**
     * 下单成功队列2
     */
    public static final String ORDER_PLACE_QUEUE2 =  "mark.order.place.queue2";

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

    /**
     * 订单手动取消队列
     */
    public static final String ORDER_CANCEL_MANUAL_QUEUE =  "mark.order.cancel.manual.queue";

    /**
     * 订单取消队列
     */
    public static final String ORDER_CANCEL_QUEUE =  "mark.order.cancel.queue";

    /**
     * 订单取消交换机
     */
    public static final String ORDER_CANCEL_EXCHANGE = "cancel.topic";

    /**
     * 订单手动取消KEY
     */
    public static final String ORDER_CANCEL_MANUAL_KEY = "cancel.manual";

    /**
     * 订单取消KEY
     */
    public static final String ORDER_CANCEL_KEY = "cancel";
}
