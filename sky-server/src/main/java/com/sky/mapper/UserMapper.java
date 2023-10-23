package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 查询所有用户
     * @return
     */
    @Select("select * from sys_user")
    List<User> getAllUsers();

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    @Select("select * from sys_user where user_name = #{userName}")
    User getUserByUserName(String userName);

    /**
     * 注册
     * @param user
     */
    //TODO INSERT语句未完成
    @Insert("insert into sys_user values")
    void insertIntoUser(User user);
}
