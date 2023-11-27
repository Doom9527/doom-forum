package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class OrdersDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //下单的商品id
    private Long[] ids;

    //备注
    private String remark;

    //手机号
    @NotBlank(message = "手机号不能为空")
    private String phone;

    //地址
    @NotBlank(message = "地址不能为空")
    private String address;

    //收货人
    @NotBlank(message = "收货人不能为空")
    private String consignee;


}
