package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersCancelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String number;

    private String cancelReason;

}
