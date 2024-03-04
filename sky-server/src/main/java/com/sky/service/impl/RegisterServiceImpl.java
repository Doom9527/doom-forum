package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;
import com.sky.exception.RegisterNullException;
import com.sky.exception.UserHasBeenRegisteredException;
import com.sky.service.RegisterService;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Objects;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO userRegisterDTO) {

        //查询该用户是否已经注册
        User userByUserName = userService.getUserByUserName(userRegisterDTO.getUserName());
        //User userByUserName = userMapper.getUserByUserName(userRegisterDTO.getUserName());
        if (!Objects.isNull(userByUserName)){
            throw new UserHasBeenRegisteredException(MessageConstant.USER_HAS_BEEN_REGISTERED);
        }

        //判断字段是否为空
        maybeNull(userRegisterDTO);

        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO,user);

        //使用BCrypt加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //添加用户并设置用户类型与密保关联
        userService.InsertUser(user, userRegisterDTO.getSecurityProblem());
    }

    private static void maybeNull(UserRegisterDTO userRegisterDTO) {
        if (!StringUtils.hasText(userRegisterDTO.getUserName())) {
            throw new RegisterNullException(MessageConstant.USERNAME_IS_NULL);
        }
        if (!StringUtils.hasText(userRegisterDTO.getPassword())) {
            throw new RegisterNullException(MessageConstant.PASSWORD_IS_NULL);
        }
//        if (!StringUtils.hasText(userRegisterDTO.getNickName())) {
//            throw new RegisterNullException(MessageConstant.NICKNAME_IS_NULL);
//        }
//        if (!StringUtils.hasText(userRegisterDTO.getEmail())) {
//            throw new RegisterNullException(MessageConstant.EMAIL_IS_NULL);
//        }
//        if (!StringUtils.hasText(userRegisterDTO.getPhonenumber())) {
//            throw new RegisterNullException(MessageConstant.PHONENUMBER_IS_NULL);
//        }
        if (!StringUtils.hasText(userRegisterDTO.getAnswer())) {
            throw new RegisterNullException(MessageConstant.ANSWER_IS_NULL);
        }
        if (userRegisterDTO.getSecurityProblem() == null) {
            throw new RegisterNullException(MessageConstant.PROBLEM_IS_NULL);
        }
    }


}
