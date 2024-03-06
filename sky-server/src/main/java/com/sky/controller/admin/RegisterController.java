package com.sky.controller.admin;

import com.sky.dto.UserRegisterDTO;
import com.sky.result.Result;
import com.sky.service.OssService;
import com.sky.service.ProblemService;
import com.sky.service.RegisterService;
import com.sky.service.UserService;
import com.sky.vo.ProblemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = "注册模块")
@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @Autowired
    private ProblemService problemService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/user/register")
    public Result<String> register(@ApiParam(value = "用户名", required = true) @RequestParam("userName") String userName,
                                   @ApiParam(value = "密码", required = true) @RequestParam("password") String password,
                                   @ApiParam(value = "密保id", required = true) @RequestParam("securityProblem") Long securityProblem,
                                   @ApiParam(value = "密保答案", required = true) @RequestParam("answer") String answer,
                                   @ApiParam(value = "头像") @RequestParam("avatar") MultipartFile avatar){
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .userName(userName)
                .password(password)
                .securityProblem(securityProblem)
                .answer(answer)
                .avatar(avatar).build();
        return registerService.register(userRegisterDTO) ? Result.success("注册成功") : Result.error("注册失败");
    }

    @ApiOperation(value = "获取密保问题")
    @GetMapping("/user/register")
    public Result<List<ProblemVO>> get() {
        List<ProblemVO> vos = problemService.getProblems();
        return Result.success(vos);
    }

}
