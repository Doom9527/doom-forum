package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.FollowService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtils;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Slf4j
@Api(tags = "用户主界面")
@RestController
@RequestMapping("/userOP")
public class UserOPController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private BlogService blogService;

    @ApiOperation(value = "查看自己关注的用户, 携带token")
    @GetMapping("/follow")
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<List<UserFollowVO>> getUsersFollowed(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        List<UserFollowVO> vos = userService.getUserFollowed(userId);
        return Result.success(vos);
    }

    @ApiOperation(value = "查看我的详情, 携带token")
    @GetMapping("/myDetail")
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<MyDetailVO> getUserDetail(HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        MyDetailVO vo = userService.getMyDetail(Long.valueOf(id));
        return Result.success(vo);
    }

    @ApiOperation(value = "查看其它用户详情, 携带token")
    @GetMapping("/detail/{userId}")
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<UserDetailVO> getUserDetail(@PathVariable Long userId, HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        UserDetailVO vo = userService.getUserDetail(userId, Long.valueOf(id));
        return Result.success(vo);
    }

    @ApiOperation(value = "查看自己的粉丝, 携带token")
    @GetMapping("/myFans")
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<List<UserFollowVO>> getUserFans(HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        List<UserFollowVO> vos = followService.selectFansById(Long.valueOf(id), Long.valueOf(id));
        return Result.success(vos);
    }

    @ApiOperation(value = "查看自己收藏过的博客, 需要携带token访问")
    @GetMapping("/favor")
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<List<BlogFavorVO>> getFavor(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        List<BlogFavorVO> vos = blogService.getBlogForFavor(Long.valueOf(userId));
        return Result.success(vos);
    }

    @ApiOperation(value = "查看自己点赞过的博客, 需要携带token访问")
    @GetMapping("/likes")
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<List<BlogFavorVO>> getLike(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        List<BlogFavorVO> vos = blogService.getBlogForFavor(Long.valueOf(userId));
        return Result.success(vos);
    }

    @ApiOperation(value = "查看自己发的博客, 需要携带token访问")
    @GetMapping
    @PreAuthorize("hasAuthority('system:redo:farmer')")
    public Result<List<BlogSentVO>> getBlogSent(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        List<BlogSentVO> vos = blogService.getBlogSent(Long.valueOf(userId));
        return Result.success(vos);
    }

    @ApiOperation(value = "用户删除博客, 需要携带token访问")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:redo:farmer')")
    public Result<String> delete(@ApiParam(value = "博客id", required = true) @PathVariable Long id, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return blogService.deleteBlogById(id, Long.valueOf(userId)) ? Result.success("删除成功") : Result.error("删除失败");
    }
}
