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
@ApiModel(description = "查看发过的博客VO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogSentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "作者id")
    private Long authorId;

    @ApiModelProperty(value = "作者名")
    private String authorName;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "分类名")
    private String categoryName;

    @JsonFormat(pattern = "用户头像地址")
    private String avatar;

    @JsonFormat(pattern = "博客图片地址")
    private String picture;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "收藏数")
    private Integer favorCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentCount;

    @ApiModelProperty(value = "审核状态: 0没通过 1通过")
    private Integer examine;

    @ApiModelProperty(value = "审核状态: 0没删除 1删除")
    private Integer status;
}