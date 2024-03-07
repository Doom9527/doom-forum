package com.sky.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sky.vo.CommentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_comment")
@Builder
public class Comment implements Serializable {

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

    @ApiModelProperty(value = "父级评论id, 如果是 0 则为一级评论")
    private Long parentId;

    @ApiModelProperty(value = "父级评论用户名")
    private String parentUserName;

    @ApiModelProperty(value = "所属一级评论id, 如果是一级评论就和自己id一样")
    private Long rootCommentId;

    @ApiModelProperty(value = "评论时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @TableField(fill = FieldFill.INSERT)
    private Date updateTime;

    private Integer status;
}