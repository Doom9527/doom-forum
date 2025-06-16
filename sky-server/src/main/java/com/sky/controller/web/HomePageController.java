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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<List<CategoriesVO>> getCategories() {
        List<CategoriesVO> vos = categoriesService.getCategories();
        return Result.success(vos);
    }

    @ApiOperation(value = "主界面获取博客")
    @GetMapping("/blog")
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<List<BlogVO>> getBlog(@ApiParam(value = "所属分类id", required = true) @Valid @RequestParam("categoryId") Long categoryId,
                                        @ApiParam(value = "模糊查询标题,可以不传") @Valid @RequestParam("title") String title,
                                        HttpServletRequest request) {
        BlogHomePageDTO dto = BlogHomePageDTO.builder()
                .categoryId(categoryId)
                .build();
        if (StrUtil.isNotBlank(title)) {
            dto.setTitle(title);
        }
        long userId = 0L;
        if (StrUtil.isNotBlank(request.getHeader("token"))) {
            userId = Long.parseLong(JwtUtils.getUserId(request.getHeader("token")));
        }
        List<BlogVO> vos = blogService.getBlogByCategoryId(dto, userId);
        return Result.success(vos);
    }

    @ApiOperation(value = "发布博客, 需要携带token访问")
    @PostMapping("/blog")
    @PreAuthorize("hasAuthority('system:redo:farmer')")
    public Result<String> publishBlog(@ApiParam(value = "博客标题", required = true) @Valid @RequestParam("title") String title,
                                      @ApiParam(value = "博客内容", required = true) @Valid @RequestParam("content") String content,
                                      @ApiParam(value = "博客分类id", required = true)@Valid @RequestParam("categoryId") Long categoryId,
                                      @ApiParam(value = "图片", required = true) @Valid @RequestPart("picture")MultipartFile picture,
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
    @PreAuthorize("hasAuthority('system:redo:tour')")
    public Result<String> like(@RequestBody BlogLikeDTO blogLikeDTO, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return likesService.likeBlog(blogLikeDTO, Long.valueOf(userId)) ? Result.success("成功") : Result.error("失败");
    }
}
