package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "角色DTO")
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @ApiModelProperty("角色ID")
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty("角色名称")
    private String roleName;

    @NotBlank(message = "角色编码不能为空")
    @ApiModelProperty("角色编码")
    private String roleKey;

    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty("显示顺序")
    private Integer roleSort;

    @ApiModelProperty("状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;
} 