package com.sky.MQInfo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderPayMQInfo {
    private List<String> redisKey;
}
