package com.sky.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "发送给前端的博客模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "作者id")
    private Long authorId;

    @ApiModelProperty(value = "作者名")
    private String authorName;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "分类名")
    private Long categoryName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date updateTime;

    @JsonFormat(pattern = "图片地址")
    private String picture;
}
