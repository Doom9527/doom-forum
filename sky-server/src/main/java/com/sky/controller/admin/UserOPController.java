package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.UserPageDTO;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserOPVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "用户管理模块")
@RestController
@RequestMapping("/userOP")
public class UserOPController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查看所有用户")
    @GetMapping
    public Result<IPage<UserOPVO>> getUsers(@RequestBody UserPageDTO dto) {
        Page<User> page = new Page<>(dto.getPageNumber(), dto.getPageSize());
        IPage<UserOPVO> data = userService.selectAll(page);
        return Result.success(data);
    }
}
