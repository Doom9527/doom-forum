package com.sky.service;

import com.sky.entity.User;
import java.util.List;

public interface UserService {

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
}
