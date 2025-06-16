package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "用户角色VO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleVO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;
}
