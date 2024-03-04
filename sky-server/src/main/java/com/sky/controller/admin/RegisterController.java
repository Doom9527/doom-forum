package com.sky.controller.admin;

import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.OssService;
import com.sky.service.ProblemService;
import com.sky.service.RegisterService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtils;
import com.sky.vo.ProblemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = "注册模块")
@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private OssService ossService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/user/register")
    public Result<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO){

        registerService.register(userRegisterDTO);
        return Result.success("注册成功");
    }

    @ApiOperation(value = "获取密保问题")
    @GetMapping("/user/register")
    public Result<List<ProblemVO>> get() {
        List<ProblemVO> vos = problemService.getProblems();
        return Result.success(vos);
    }

    @ApiOperation(value = "用户修改头像")
    @PostMapping("/user/register/upload")
    public Result<String> uploadOssFile(MultipartFile file, HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        User user = userService.getUserById(Long.valueOf(id));
        // MultipartFile -> 获取上传文件
        // 返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file, user.getUserName());
        return Result.success("上传路径: " + url);
    }
}
