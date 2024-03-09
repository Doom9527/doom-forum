package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "查看博客分页DTO")
public class BlogPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页号")
    private Integer pageNumber = 1;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "按分类id查, 不填就是所有")
    private Long categoryId;

    @ApiModelProperty(value = "按用户id查, 不填就是所有")
    private Long userId;

    @ApiModelProperty(value = "按标题模糊搜索, 不填就是所有")
    private String title;

    @ApiModelProperty(value = "审核状态: 1通过 0未通过, 不填就是所有")
    private Integer examine;

    @ApiModelProperty(value = "删除状态: 1通过 0 未通过, 不填就是所有")
    private Integer status;
}
