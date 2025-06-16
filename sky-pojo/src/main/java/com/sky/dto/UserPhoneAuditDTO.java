package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "管理员审核用户手机号时传递的数据模型")
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneAuditDTO {
    @NotNull(message = "用户ID为空")
    @ApiModelProperty("用户ID")
    private Long userId;

    @NotNull(message = "审核状态为空")
    @ApiModelProperty("审核状态：0-待审核，1-已通过，2-已拒绝")
    private Integer phoneStatus;
} 