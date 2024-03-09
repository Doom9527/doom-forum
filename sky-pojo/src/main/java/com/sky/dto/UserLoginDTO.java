package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(description = "用户登陆时传递的数据模型")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    @NotBlank(message = "密码为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotNull(message = "用户类型为空")
    @ApiModelProperty(value = "用户类型: 1用户 2游客 3管理员", required = true)
    private Character userType;

    @NotBlank(message = "验证码为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String code;

}
