package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Problem;
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

    /**
     * 根据id查询密保问题
     * @param id
     * @return
     */
    Problem getProblemByUserId(String id);

    /**
     * 验证密保问题
     * @param answer
     * @return
     */
    Boolean checkAnswer(String answer, String userId);

    /**
     * 修改密码
     * @param password
     * @param userId
     * @return
     */
    boolean modifyPassword(String password, String userId);
}
