package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;
import com.sky.exception.UserHasBeenRegisteredException;
import com.sky.mapper.UserMapper;
import com.sky.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    @Override
    public void register(UserRegisterDTO userRegisterDTO) {

        //查询该用户是否已经注册
        User userByUserName = userMapper.getUserByUserName(userRegisterDTO.getUserName());
        if (!Objects.isNull(userByUserName)){
            throw new UserHasBeenRegisteredException(MessageConstant.USER_HAS_BEEN_REGISTERED);
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO,user);
        user.setPassword("");
    }
}
