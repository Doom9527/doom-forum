package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@ApiModel(description = "添加评论DTO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博客id", required = true)
    private Long postId;

    @ApiModelProperty(value = "评论内容", required = true)
    private String content;
}
