package com.sky.controller.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.UserPageDTO;
import com.sky.entity.Blog;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.CommentService;
import com.sky.service.UserService;
import com.sky.vo.*;
import com.sky.dto.BlogPageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@Api(tags = "管理员模块,大部分管理员操作都在这")
@RequestMapping("/OP")
public class BlogOPController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "管理员查看所有博客, 需要携带token访问")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('system:admin:list')")
    public Result<IPage<BlogOPVO>> getAllBlogs(@RequestBody BlogPageDTO dto) {
        Page<Blog> page = new Page<>(dto.getPageNumber(), dto.getPageSize());
        IPage<BlogOPVO> data = blogService.getAllBlogsOP(page, dto);
        return Result.success(data);
    }

    @ApiOperation(value = "管理员通过审核博客, 需要携带token访问")
    @PutMapping("/{ids}")
    @PreAuthorize("hasAuthority('system:admin:list')")
    public Result<Integer> pass(@ApiParam(value = "博客id数组", required = true) @PathVariable Long[] ids) {
        return Result.success(blogService.passBlogById(ids));
    }

    @ApiOperation(value = "管理员删除博客, 需要携带token访问")
    @PutMapping("/delete2/{ids}")
    @PreAuthorize("hasAuthority('system:admin:list')")
    public Result<Integer> delete(@ApiParam(value = "博客id数组", required = true) @PathVariable Long[] ids) {
        return Result.success(blogService.deleteBlogByIds(ids));
    }

    @ApiOperation(value = "管理员恢复删除的博客, 需要携带token访问")
    @PutMapping("/recover/{ids}")
    @PreAuthorize("hasAuthority('system:admin:list')")
    public Result<Integer> recover(@ApiParam(value = "博客id数组", required = true) @PathVariable Long[] ids) {
        return Result.success(blogService.recoverBlogByIds(ids));
    }

    @ApiOperation(value = "修改评论状态, 可以选择删除或恢复, 需要携带token访问")
    @PutMapping("/comment")
    @PreAuthorize("hasAuthority('system:admin:list')")
    public Result<String> checkComments(@ApiParam(value = "删除状态(和其他的不一样): 0未删除 1删除") @RequestParam Integer status,
                                        @ApiParam(value = "评论id") @RequestParam Integer commentId) {
        return commentService.modifyComments(commentId, status) ? Result.success("修改成功") : Result.error("修改失败");
    }

    @ApiOperation(value = "管理员查看所有用户, 需要携带token访问")
    @GetMapping
    @PreAuthorize("hasAuthority('system:admin:list')")
    public Result<IPage<UserOPVO>> getUsers(@RequestBody UserPageDTO dto) {
        Page<User> page = new Page<>(dto.getPageNumber(), dto.getPageSize());
        IPage<UserOPVO> data = userService.selectAll(page);
        return Result.success(data);
    }
}
