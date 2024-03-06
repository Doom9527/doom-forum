package com.sky.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "发送给前端的博客模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "作者id")
    private Long authorId;

    @ApiModelProperty(value = "作者名")
    private String authorName;

    @JsonFormat(pattern = "用户头像地址")
    private String avatar;

    @JsonFormat(pattern = "博客图片地址")
    private String picture;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "收藏数")
    private Integer collectCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentCount;
}
