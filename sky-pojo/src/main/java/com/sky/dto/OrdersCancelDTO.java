package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class OrdersCancelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "订单号不能为空")
    private String number;

    @NotBlank(message = "取消原因不能为空")
    private String cancelReason;

}
