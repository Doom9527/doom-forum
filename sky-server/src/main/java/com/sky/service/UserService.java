package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 查询所有用户
     * @return
     */
    List<User> getAllUsers();

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 注册，添加用户
     */
    void InsertUser(User user, Long id);

    /**
     * 根据id查询用户
     */
    User getUserById(Long id);
}
