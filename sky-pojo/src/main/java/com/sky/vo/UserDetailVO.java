package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查看其它用户的详情VO")
public class UserDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "关注数")
    private Long followCount;

    @ApiModelProperty(value = "粉丝数")
    private Long fansCount;

    @ApiModelProperty(value = "获赞与收藏")
    private Long totalCount;

    @ApiModelProperty(value = "关注状态: 1关注 0未关注")
    private Integer status;

}