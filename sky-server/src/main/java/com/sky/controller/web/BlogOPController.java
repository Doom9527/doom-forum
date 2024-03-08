package com.sky.controller.web;

import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.utils.JwtUtils;
import com.sky.vo.BlogFavorVO;
import com.sky.vo.BlogSentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "博客管理页面")
@RequestMapping("/OP")
public class BlogOPController {

    @Autowired
    private BlogService blogService;

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

    @ApiParam(value = "审核博客")
    @PutMapping
    public Result<String> examine(@ApiParam(value = "博客id", required = true) Long id,
                                  @ApiParam(value = "审核标记: 1通过 0不通过", required = true) Integer flag) {
        return blogService.examineBlogById(id, flag) ? Result.success("审核成功") : Result.error("审核失败");
    }

    @ApiOperation(value = "删除博客, 需要携带token访问")
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@ApiParam(value = "博客id", required = true) Long id, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return blogService.deleteBlogById(id, Long.valueOf(userId)) ? Result.success("删除成功") : Result.error("删除失败");
    }
}
