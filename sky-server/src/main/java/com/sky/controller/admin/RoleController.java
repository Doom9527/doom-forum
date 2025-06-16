package com.sky.controller.admin;

import com.sky.dto.UserRoleDTO;
import com.sky.entity.Role;
import com.sky.result.Result;
import com.sky.service.RoleService;
import com.sky.vo.RoleVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/role")
@Api(tags = "角色管理接口")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:redo:admin')")
    @ApiOperation("获取所有角色列表")
    public Result<List<RoleVO>> list() {
        List<RoleVO> roles = roleService.listAll();
        return Result.success(roles);
    }

    @PutMapping("/users")
    @PreAuthorize("hasAuthority('system:redo:admin')")
    @ApiOperation("批量更新用户角色")
    public Result<Void> batchUpdateUserRoles(@RequestBody @Valid UserRoleDTO userRoleDTO) {
        roleService.batchUpdateUserRoles(userRoleDTO.getUserIds(), userRoleDTO.getRoleId());
        return Result.success();
    }
} 