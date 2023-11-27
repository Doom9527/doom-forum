package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersVO implements Serializable {

    /**
     * 订单状态 1待付款 2已完成 3已取消
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer COMPLETED = 2;
    public static final Integer CANCELLED = 3;

    /**
     * 支付状态 0未支付 1已支付 2退款
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    //下单的商品id
    private Long[] ids;

    //订单号
    private String number;

    //订单状态 1待付款 2已完成 3已取消
    private Integer status;

    //下单时间
    private LocalDateTime orderTime;

    //结账时间
    private LocalDateTime checkoutTime;

    //支付状态 0未支付 1已支付
    private Integer payStatus;

    //实收金额
    private BigDecimal amount;

    //备注
    private String remark;

    //用户名
    private String userName;

    //手机号
    private String phone;

    //地址
    private String address;

    //收货人
    private String consignee;

    //订单取消原因
    private String cancelReason;

    //订单取消时间
    private LocalDateTime cancelTime;

}