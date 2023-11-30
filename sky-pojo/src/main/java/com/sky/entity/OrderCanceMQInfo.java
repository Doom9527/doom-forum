package com.sky.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderCanceMQInfo {
    private Long[] ids;
    private List<String> redisKey;
}
