package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{id}, 3)")
    void relateUserAndRole (Long id);
}
