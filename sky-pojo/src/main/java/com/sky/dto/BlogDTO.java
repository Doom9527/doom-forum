package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ApiModel(description = "博客传递的数据模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "图片")
    private MultipartFile picture;

    @ApiModelProperty(value = "作者id")
    private Long authorId;
}
