package com.sky.service.impl;

import com.sky.entity.User;
import com.sky.mapper.MenuMapper;
import com.sky.mapper.UserMapper;
import com.sky.testPojo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        User user = userMapper.getUserByUserName(userName);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        //List<String> list = new ArrayList<>(Arrays.asList("test"));
        //封装成UserDetails对象返回
        return new LoginUser(user,list);
    }
}
