package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Role;
import com.sky.entity.UserRole;
import com.sky.mapper.RoleMapper;
import com.sky.mapper.UserRoleMapper;
import com.sky.service.RoleService;
import com.sky.vo.RoleVO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<RoleVO> listAll() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getDelFlag, 0)
               .eq(Role::getStatus, "0")
               .orderByAsc(Role::getCreateTime);

        return this.list(wrapper).stream().map(role -> RoleVO.builder()
                .id(role.getId())
                .name(role.getName())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateUserRoles(List<Long> userIds, Long roleId) {
        for (Long userId : userIds) {
            userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
            userRoleMapper.insert(new UserRole(userId, roleId));
        }
    }
} 