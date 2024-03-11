package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "关注用户传递的数据模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFollowDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关注用户id", required = true)
    private Long userFollowId;

    @ApiModelProperty(value = "关注状态: 0未关注 1已关注 (这里上传的是目前的状态,比如上传1,就会取消关注改成0)", required = true)
    private Integer status;
}
