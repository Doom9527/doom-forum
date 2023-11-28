package com.sky.controller.admin;

import com.sky.dto.UserRegisterDTO;
import com.sky.result.Result;
import com.sky.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/user/register")
    public Result<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO){

        registerService.register(userRegisterDTO);

        return Result.success("注册成功");
    }
}
