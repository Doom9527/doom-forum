package com.sky.controller.admin;
import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.service.LoginService;
import com.sky.utils.CaptchaUtils;
import com.sky.utils.IPUtils;
import com.sky.utils.RedisCache;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


@Slf4j
@Api(tags = "登录模块")
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisCache redisCache;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){
        //User user = loginService.SearchLoginUser(userLoginDTO);
        String ipAddr = IPUtils.getIpAddr(request);
        UserLoginVO userLoginVO = loginService.login(userLoginDTO, ipAddr);
        return Result.success(userLoginVO);
    }

    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public Result<String> logout(){
        loginService.logout();
        return Result.success("退出成功");
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("/getCaptcha")
    public Result<String> getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = CaptchaUtils.setCaptcha(response);
        String ipAddr = IPUtils.getIpAddr(request);
        redisCache.setCacheObject(ipAddr, code);
        redisCache.expire(ipAddr, 60);
        return Result.success();
    }
}
