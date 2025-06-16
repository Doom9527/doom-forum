package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "用户角色DTO")
public class UserRoleDTO {
    @ApiModelProperty("用户ID列表")
    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> userIds;

    @ApiModelProperty("角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
} 