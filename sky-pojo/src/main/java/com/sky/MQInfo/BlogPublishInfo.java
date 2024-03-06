package com.sky.MQInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogPublishInfo {
    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "作者id")
    private Long authorId;
}
