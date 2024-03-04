package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<User> getAllUsers() {
        //return userMapper.getAllUsers();
        return baseMapper.selectList(null);
    }


    @Override
    public User getUserByUserName(String userName) {
        //return userMapper.getUserByUserName(userName);
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    @Transactional
    public void InsertUser(User user, Long problem) {
        baseMapper.insert(user);
        User user1 = getUserByUserName(user.getUserName());
        baseMapper.relateUserAndRole(user1.getId());
        baseMapper.relateUserAndProblem(user.getId(), problem);
    }

    @Override
    public User getUserById(Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, id);
        return baseMapper.selectOne(wrapper);
    }


}
