package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Role;
import com.sky.vo.RoleVO;
import com.sky.dto.UserRoleDTO;

import java.util.List;

public interface RoleService extends IService<Role> {
    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    List<RoleVO> listAll();

    /**
     * 批量更新用户角色
     * @param userIds 用户ID列表
     * @param roleId 角色ID
     */
    void batchUpdateUserRoles(List<Long> userIds, Long roleId);
} 