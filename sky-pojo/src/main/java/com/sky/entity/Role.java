package com.sky.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("sys_role")
@ApiModel(description = "角色实体")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色编码")
    private String roleKey;

    @ApiModelProperty("状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    @ApiModelProperty("创建者")
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("备注")
    private String remark;
} 