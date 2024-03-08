package com.sky.controller.web;

import com.sky.dto.BlogFavorDTO;
import com.sky.dto.BlogLikeDTO;
import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.FavorService;
import com.sky.service.LikesService;
import com.sky.utils.JwtUtils;
import com.sky.vo.BlogDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@Api(tags = "博客详情: 这里点赞和主页面点赞是不一样的,需要分开进行")
@RequestMapping("/detail")
public class BlogDetailController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private FavorService favorService;

    @ApiOperation(value = "查看博客详情")
    @GetMapping("/{blogId}")
    public Result<BlogDetailVO> getDetail(@PathVariable Long blogId, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        BlogDetailVO vo = blogService.getBlogByBlogId(Long.valueOf(userId), blogId);
        return Result.success(vo);
    }

    @ApiOperation(value = "收藏或取消收藏: 收藏再点一下就会取消, 需要携带token访问")
    @PutMapping("/favor")
    public Result<String> favor(@RequestBody BlogFavorDTO blogFavorDTO, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return favorService.favorBlogDetail(blogFavorDTO, Long.valueOf(userId)) ? Result.success("收藏成功") : Result.error("收藏失败");
    }
}
