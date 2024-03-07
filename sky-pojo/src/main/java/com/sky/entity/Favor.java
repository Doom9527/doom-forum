package com.sky.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_favor")
@Builder
public class Favor implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Integer LIKE = 1;

    private static final Integer UN_LIKE = 0;

    private Long id;

    private Long postId;

    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Integer status;
}