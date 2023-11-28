package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(description = "用户登陆时传递的数据模型")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO implements Serializable {

    @NotBlank(message = "用户名为空")
    @ApiModelProperty("用户名")
    private String userName;

    @NotBlank(message = "密码为空")
    @ApiModelProperty("密码")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码为空")
    private String code;

}
