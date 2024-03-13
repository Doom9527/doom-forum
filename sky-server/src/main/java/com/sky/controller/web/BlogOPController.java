package com.sky.controller.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.entity.Blog;
import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.CommentService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtils;
import com.sky.vo.BlogFavorVO;
import com.sky.vo.BlogOPVO;
import com.sky.dto.BlogPageDTO;
import com.sky.vo.BlogSentVO;
import com.sky.vo.UserFollowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@Api(tags = "博客管理页面")
@RequestMapping("/OP")
public class BlogOPController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查看自己收藏过的博客, 需要携带token访问")
    @GetMapping("/favor")
    public Result<List<BlogFavorVO>> getFavor(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        List<BlogFavorVO> vos = blogService.getBlogForFavor(Long.valueOf(userId));
        return Result.success(vos);
    }

    @ApiOperation(value = "查看自己发的博客, 需要携带token访问")
    @GetMapping
    public Result<List<BlogSentVO>> getBlogSent(HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        List<BlogSentVO> vos = blogService.getBlogSent(Long.valueOf(userId));
        return Result.success(vos);
    }

    @ApiOperation(value = "管理员查看所有博客, 需要携带token访问")
    @GetMapping("/all")
    public Result<IPage<BlogOPVO>> getAllBlogs(@RequestBody BlogPageDTO dto) {
        Page<Blog> page = new Page<>(dto.getPageNumber(), dto.getPageSize());
        IPage<BlogOPVO> data = blogService.getAllBlogsOP(page, dto);
        return Result.success(data);
    }

    @ApiOperation(value = "管理员通过审核博客, 需要携带token访问")
    @PutMapping("/{ids}")
    public Result<Integer> pass(@ApiParam(value = "博客id数组", required = true) @PathVariable Long[] ids) {
        return Result.success(blogService.passBlogById(ids));
    }

    @ApiOperation(value = "管理员删除博客, 需要携带token访问")
    @PutMapping("/delete2/{ids}")
    public Result<Integer> delete(@ApiParam(value = "博客id数组", required = true) @PathVariable Long[] ids) {
        return Result.success(blogService.deleteBlogByIds(ids));
    }

    @ApiOperation(value = "管理员恢复删除的博客, 需要携带token访问")
    @PutMapping("/recover/{ids}")
    public Result<Integer> recover(@ApiParam(value = "博客id数组", required = true) @PathVariable Long[] ids) {
        return Result.success(blogService.recoverBlogByIds(ids));
    }

    @ApiOperation(value = "用户删除博客, 需要携带token访问")
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@ApiParam(value = "博客id", required = true) @PathVariable Long id, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return blogService.deleteBlogById(id, Long.valueOf(userId)) ? Result.success("删除成功") : Result.error("删除失败");
    }

    @ApiOperation(value = "修改评论状态, 可以选择删除或恢复, 需要携带token访问")
    @PutMapping("/comment")
    public Result<String> checkComments(@ApiParam(value = "删除状态(和其他的不一样): 0未删除 1删除") @RequestParam Integer status,
                                        @ApiParam(value = "评论id") @RequestParam Integer commentId) {
        return commentService.modifyComments(commentId, status) ? Result.success("修改成功") : Result.error("修改失败");
    }
}
