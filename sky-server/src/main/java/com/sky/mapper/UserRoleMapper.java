package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    void insertBatch(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    Long selectRoleIdsByUserId(Long userId);

    @Update("<script>" +
            "INSERT INTO sys_user_role (user_id, role_id) VALUES " +
            "<foreach collection='userIds' item='userId' separator=','>" +
            "(#{userId}, #{roleId})" +
            "</foreach>" +
            "ON DUPLICATE KEY UPDATE role_id = #{roleId}" +
            "</script>")
    void batchUpdateUserRole(@Param("userIds") List<Long> userIds, @Param("roleId") Long roleId);
} 