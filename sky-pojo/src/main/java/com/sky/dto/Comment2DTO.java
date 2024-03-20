package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(description = "添加二级及其子评论DTO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment2DTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "博客id", required = true)
    private Long postId;

    @NotBlank
    @ApiModelProperty(value = "评论内容", required = true)
    private String content;

    @NotNull
    @ApiModelProperty(value = "回复评论id", required = true)
    private Long rootCommentId;

    @NotNull
    @ApiModelProperty(value = "回复谁的评论", required = true)
    private Long parentId;
}
