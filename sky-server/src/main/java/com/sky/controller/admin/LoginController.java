package com.sky.controller.admin;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.LoginService;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/user/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        //User user = loginService.SearchLoginUser(userLoginDTO);
        UserLoginVO userLoginVO = loginService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    @RequestMapping("/user/logout")
    public Result<String> logout(){
        loginService.logout();
        return Result.success("退出成功");
    }
}
