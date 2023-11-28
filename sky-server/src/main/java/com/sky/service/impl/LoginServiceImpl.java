package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.exception.UserNameOrPasswordErrorException;
import com.sky.service.LoginService;
import com.sky.testPojo.LoginUser;
import com.sky.utils.CaptchaUtils;
import com.sky.utils.JwtUtils;
import com.sky.utils.RedisCache;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO, Object ipAddr) {
        //验证码校验
        if (CaptchaUtils.verifyCaptcha(userLoginDTO.getCode(), ipAddr)) {
            throw new LoginFailedException(MessageConstant.VERIFY_CODE_ERROR);
        }
        redisCache.deleteObject((String) ipAddr);

        //对象拷贝
        User user = new User();
        BeanUtils.copyProperties(userLoginDTO,user);

        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)){
            throw new UserNameOrPasswordErrorException(MessageConstant.USERNAMEORPASSWORD_ERROR);
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        Map<String,Object> info = new HashMap<>();
        info.put("userName",user.getUserName());
        String token = JwtUtils.sign(userId, info);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //返回UserLoginDTO
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .build();
        return userLoginVO;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
    }

}
