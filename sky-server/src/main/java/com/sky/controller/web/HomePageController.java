package com.sky.controller.web;

import cn.hutool.core.util.StrUtil;
import com.sky.dto.BlogDTO;
import com.sky.dto.BlogHomePageDTO;
import com.sky.dto.BlogLikeDTO;
import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.CategoriesService;
import com.sky.service.LikesService;
import com.sky.utils.JwtUtils;
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

@CrossOrigin
@Slf4j
@RestController
@Api(tags = "主页面")
@RequestMapping("/home")
public class HomePageController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private LikesService likesService;

    @ApiOperation(value = "获取所有分类")
    @GetMapping("/cate")
    public Result<List<CategoriesVO>> getCategories() {
        List<CategoriesVO> vos = categoriesService.getCategories();
        return Result.success(vos);
    }

    @ApiOperation(value = "主界面获取博客, 这里不携带token访问的话就是游客页面, 看不到自己点赞过的文章")
    @GetMapping("/blog")
    public Result<List<BlogVO>> getBlog(@RequestBody BlogHomePageDTO dto, HttpServletRequest request) {
        long userId = 0L;
        if (StrUtil.isNotBlank(request.getHeader("token"))) {
            userId = Long.parseLong(JwtUtils.getUserId(request.getHeader("token")));
        }
        List<BlogVO> vos = blogService.getBlogByCategoryId(dto, userId);
        return Result.success(vos);
    }

    @ApiOperation(value = "发布博客, 需要携带token访问")
    @PostMapping("/blog")
    public Result<String> publishBlog(@ApiParam(value = "博客标题", required = true) @Valid @RequestParam("title") String title,
                                      @ApiParam(value = "博客内容", required = true) @Valid @RequestParam("content") String content,
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

    @ApiOperation(value = "点赞或取消点赞: 点一下就会转换点赞状态, 需要携带token访问")
    @PutMapping("/blog")
    public Result<String> like(@RequestBody BlogLikeDTO blogLikeDTO, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return likesService.likeBlog(blogLikeDTO, Long.valueOf(userId)) ? Result.success("成功") : Result.error("失败");
    }
}
