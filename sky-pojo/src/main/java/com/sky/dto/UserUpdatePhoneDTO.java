package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "用户修改手机号时传递的数据模型")
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatePhoneDTO {
    @NotBlank(message = "手机号为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty("新手机号")
    private String phonenumber;
} 