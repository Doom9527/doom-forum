package com.sky.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "通知评论VO, 在这里回复直接调用二级回复评论接口")
public class NoticeCommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回复的是笔记还是评论, 1笔记 0评论")
    private Integer flag;

    @ApiModelProperty(value = "新增的评论id")
    private Long newId;

    @ApiModelProperty(value = "新增的评论内容")
    private String newContent;

    @ApiModelProperty(value = "被回复的评论id(如果回复笔记就为空)")
    private Long oldId;

    @ApiModelProperty(value = "被回复的评论内容(如果回复笔记就为空)")
    private String oldContent;

    @ApiModelProperty(value = "所属一级评论id, 0为一级评论")
    private Long rootCommentId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "笔记id")
    private Long postId;

    @ApiModelProperty(value = "笔记图片")
    private String picture;

    @ApiModelProperty(value = "是否为作者本人回复: 1是 0不是")
    private Integer status;

    @ApiModelProperty(value = "时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
}