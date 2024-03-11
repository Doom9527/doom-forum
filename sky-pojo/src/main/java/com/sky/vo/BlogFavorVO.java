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
@ApiModel(description = "收藏博客模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogFavorVO implements Serializable {
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

    @ApiModelProperty(value = "收藏时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    @ApiModelProperty(value = "收藏状态: 0没收藏 1收藏")
    private Integer status;

    @ApiModelProperty(value = "是不是自己, 是的话肯定就不显示关注了: 1是 0不是")
    private Integer flag;

    @ApiModelProperty(value = "关注状态: 0没关 1关了")
    private Integer status3;
}
