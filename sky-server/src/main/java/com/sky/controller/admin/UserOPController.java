package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.UserPageDTO;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.FollowService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtils;
import com.sky.vo.MyDetailVO;
import com.sky.vo.UserDetailVO;
import com.sky.vo.UserFollowVO;
import com.sky.vo.UserOPVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Slf4j
@Api(tags = "用户管理模块")
@RestController
@RequestMapping("/userOP")
public class UserOPController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @ApiOperation(value = "管理员查看所有用户, 携带token")
    @GetMapping
    public Result<IPage<UserOPVO>> getUsers(@RequestBody UserPageDTO dto) {
        Page<User> page = new Page<>(dto.getPageNumber(), dto.getPageSize());
        IPage<UserOPVO> data = userService.selectAll(page);
        return Result.success(data);
    }

    @ApiOperation(value = "查看自己关注的用户, 携带token")
    @GetMapping("/follow")
    public Result<List<UserFollowVO>> getUsersFollowed(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        List<UserFollowVO> vos = userService.getUserFollowed(userId);
        return Result.success(vos);
    }

    @ApiOperation(value = "查看我的详情, 携带token")
    @GetMapping("/myDetail")
    public Result<MyDetailVO> getUserDetail(HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        MyDetailVO vo = userService.getMyDetail(Long.valueOf(id));
        return Result.success(vo);
    }

    @ApiOperation(value = "查看其它用户详情, 携带token")
    @GetMapping("/detail/{userId}")
    public Result<UserDetailVO> getUserDetail(@PathVariable Long userId, HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        UserDetailVO vo = userService.getUserDetail(userId, Long.valueOf(id));
        return Result.success(vo);
    }

    @ApiOperation(value = "查看自己的粉丝, 携带token")
    @GetMapping("/myFans")
    public Result<List<UserFollowVO>> getUserFans(HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        List<UserFollowVO> vos = followService.selectFansById(Long.valueOf(id), Long.valueOf(id));
        return Result.success(vos);
    }
}
