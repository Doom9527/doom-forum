package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    @NotBlank(message = "密码为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

//    @NotBlank(message = "昵称为空")
//    @ApiModelProperty("昵称")
//    private String nickName;
//
//    @NotBlank(message = "手机号为空")
//    @ApiModelProperty("手机号")
//    private String phonenumber;
//
//    @NotBlank(message = "邮箱为空")
//    @Email(message = "邮箱格式不正确")
//    @ApiModelProperty("邮箱号")
//    private String email;
//
//    @NotNull(message = "性别为空")
//    @ApiModelProperty("性别")
//    private Integer sex;

    @NotNull(message = "密保问题为空")
    @ApiModelProperty(value = "密保问题", required = true)
    private Long securityProblem;

    @NotBlank(message = "密保答案为空")
    @ApiModelProperty(value = "密保答案", required = true)
    private String answer;

    @ApiModelProperty(value = "头像")
    private MultipartFile avatar;
}
