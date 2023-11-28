package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(description = "用户注册时传递的数据模型")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO implements Serializable {
    @NotBlank(message = "用户名为空")
    @ApiModelProperty("用户名")
    private String userName;

    @NotBlank(message = "密码为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "昵称为空")
    @ApiModelProperty("昵称")
    private String nickName;

    @NotBlank(message = "手机号为空")
    @ApiModelProperty("手机号")
    private String phonenumber;

    @NotBlank(message = "邮箱为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty("邮箱号")
    private String email;

    @NotNull(message = "性别为空")
    @ApiModelProperty("性别")
    private Integer sex;

}
