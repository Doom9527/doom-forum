package com.sky.controller.admin;

import com.sky.dto.UserRegisterDTO;
import com.sky.result.Result;
import com.sky.service.impl.RegisterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class RegisterController {

    @Resource
    private RegisterServiceImpl registerServiceImpl;

    @PostMapping("/user/register")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO){

        registerServiceImpl.register(userRegisterDTO);

        return Result.success("注册成功");
    }
}
