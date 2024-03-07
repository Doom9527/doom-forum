package com.sky.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("二级评论以及所有子集回复")
public class CommentVO2 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论主键")
    private Long id;

    @ApiModelProperty(value = "博客id")
    private Long postId;

    @ApiModelProperty(value = "评论者id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "回复评论id, 0为一级评论")
    private Long parentId;

    @ApiModelProperty(value = "回复评论用户名")
    private String parentUserName;

    @ApiModelProperty(value = "所属一级评论id, 0为一级评论")
    private Long rootCommentId;

    @ApiModelProperty(value = "评论时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否为一级评论本人回复: 1是 0否")
    private Integer flag1;

    @ApiModelProperty(value = "是否为二级评论: 1是 0否")
    private Integer flag2;
}