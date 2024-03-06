package com.sky.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_categories")
@Builder
public class Categories implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private String status;
}
