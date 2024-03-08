package com.sky.controller.admin;

import com.sky.dto.PageDTO;
import com.sky.result.PageQuery;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserOPVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Slf4j
@Api(tags = "用户管理模块")
@RestController
@RequestMapping("/userOP")
public class UserOPController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查看所有用户")
    @GetMapping
    public Result<PageDTO<UserOPVO>> getUsers(@Valid PageQuery query) {
        PageDTO<UserOPVO> vos = userService.selectAll(query);
        return Result.success(vos);
    }
}
