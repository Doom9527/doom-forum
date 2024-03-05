package com.sky.controller.admin;

import com.sky.entity.Problem;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.OssService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Api(tags = "用户信息模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OssService ossService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户修改头像: 用户登录后进行,携带token")
    @PostMapping("/upload")
    public Result<String> uploadOssFile(MultipartFile file, HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        User user = userService.getUserById(Long.valueOf(id));
        // MultipartFile -> 获取上传文件
        // 返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file, user.getUserName());
        return Result.success("上传路径: " + url);
    }

    @ApiOperation(value = "查询用户密保问题: 用户登录后进行,携带token")
    @GetMapping("/check")
    public Result<Problem> getAnswer(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        Problem problem = userService.getProblemByUserId(userId);
        return Result.success(problem);
    }

    @ApiOperation(value = "验证密保问题: 用户登录后进行,携带token. true成功,false失败. 成功后在30分钟内修改密码,否则过期失效")
    @PostMapping("/check")
    public Result<Boolean> check(@Valid @RequestParam String answer, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        Boolean flag = userService.checkAnswer(answer, userId);
        return Result.success(flag);
    }

    @ApiOperation(value = "修改密码: 用户登录后进行,携带token")
    @PutMapping("/modify")
    public Result<String> modify(@Valid @RequestParam String password, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return userService.modifyPassword(password, userId) ? Result.success() : Result.error("0");
    }

    @ApiOperation(value = "查询用户密保问题: 登录前进行,无token")
    @GetMapping("/check2/{userName}")
    public Result<Problem> getAnswer(@Valid @PathVariable String userName) {
        User user = userService.getUserByUserName(userName);
        Problem problem = userService.getProblemByUserId(user.getId().toString());
        return Result.success(problem);
    }

    @ApiOperation(value = "验证密保问题: 登录前进行,无token. true成功,false失败. 成功后在30分钟内修改密码,否则过期失效")
    @PostMapping("/check2")
    public Result<Boolean> check(@Valid @RequestParam String answer, @Valid @RequestParam String userName) {
        User user = userService.getUserByUserName(userName);
        Boolean flag = userService.checkAnswer(answer, user.getId().toString());
        return Result.success(flag);
    }

    @ApiOperation(value = "修改密码: 登录前进行,无token")
    @PutMapping("/modify2")
    public Result<String> modify(@Valid @RequestParam String password, @Valid @RequestParam String userName) {
        User user = userService.getUserByUserName(userName);;
        return userService.modifyPassword(password, user.getId().toString()) ? Result.success() : Result.error("0");
    }

}
