package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@ApiModel(description = "主界面博客DTO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogHomePageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属分类id", required = true)
    private Long categoryId;

    @ApiModelProperty(value = "模糊查询标题,可以不传")
    private String title;
}