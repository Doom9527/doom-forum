package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "点赞博客传递的数据模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogFavorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博客id", required = true)
    private Long postId;

    @ApiModelProperty(value = "收藏状态: 0未收藏 1已收藏(这里上传的是目前的状态,比如上传1,就会取消收藏改成0)", required = true)
    private Integer status;
}