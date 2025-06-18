package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "我的详情VO")
public class MyDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "手机号")
    private String phonenumber;

    @ApiModelProperty(value = "个人信息审核状态")
    private Integer phoneStatus;

    @ApiModelProperty(value = "关注数")
    private Long followCount;

    @ApiModelProperty(value = "粉丝数")
    private Long fansCount;

    @ApiModelProperty(value = "获赞与收藏")
    private Integer totalCount;
}