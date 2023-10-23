package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "用户注册时传递的数据模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO implements Serializable {
    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String phonenumber;

    @ApiModelProperty("邮箱号")
    private String email;

    @ApiModelProperty("性别")
    private Integer sex;

}
