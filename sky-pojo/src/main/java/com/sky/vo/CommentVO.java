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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("传递给前端的评论VO,包括一级评论和二级评论以及所有子集回复")
public class CommentVO implements Serializable {
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

    @ApiModelProperty(value = "二级评论及其子回复")
    private List<CommentVO2> replyComments = new ArrayList<>();
}
