package com.sky.controller.web;

import com.sky.dto.BlogDTO;
import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.CategoriesService;
import com.sky.utils.JwtUtils;
import com.sky.vo.BlogDetailVO;
import com.sky.vo.BlogVO;
import com.sky.vo.CategoriesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "主页")
@RequestMapping("/home")
public class HomePageController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoriesService categoriesService;

    @ApiOperation(value = "主界面获取博客")
    @GetMapping("/blog/{categoryId}")
    public Result<List<BlogVO>> getBlog(@PathVariable Long categoryId) {
        List<BlogVO> vos = blogService.getBlogByCategoryId(categoryId);
        return Result.success(vos);
    }

    @ApiOperation(value = "查看博客详情")
    @GetMapping("/detail/{blogId}")
    public Result<BlogDetailVO> getDetail(@PathVariable Long blogId) {
        return null;
    }

    @ApiOperation(value = "发布博客")
    @PostMapping("/blog")
    public Result<String> publishBlog(@ApiParam(value = "博客标题", required = true) @Valid @RequestParam("title") String title,
                                      @ApiParam(value = "博客内容", required = true)@Valid @RequestParam("content") String content,
                                      @ApiParam(value = "博客分类id", required = true)@Valid @RequestParam("categoryId") Long categoryId,
                                      @RequestParam("picture")MultipartFile picture,
                                      HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        BlogDTO blogDTO = BlogDTO.builder()
                .title(title)
                .content(content)
                .categoryId(categoryId)
                .picture(picture)
                .authorId(Long.valueOf(userId))
                .build();
        return blogService.publishBlog(blogDTO) ? Result.success("发布成功，管理员审核中") : Result.error("发布失败");
    }

    @ApiOperation(value = "获取所有分类")
    @GetMapping("/cate")
    public Result<List<CategoriesVO>> getCategories() {
        List<CategoriesVO> vos = categoriesService.getCategories();
        return Result.success(vos);
    }

}
