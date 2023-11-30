package com.sky.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderPlaceMQInfo {
    private Long[] ids;
    private List<String> arr;
}
