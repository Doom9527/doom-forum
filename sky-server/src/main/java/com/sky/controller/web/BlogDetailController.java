package com.sky.controller.web;

import com.sky.dto.BlogFavorDTO;
import com.sky.dto.BlogLikeDTO;
import com.sky.dto.Comment2DTO;
import com.sky.dto.CommentDTO;
import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.CommentService;
import com.sky.service.FavorService;
import com.sky.service.LikesService;
import com.sky.utils.JwtUtils;
import com.sky.vo.BlogDetailVO;
import com.sky.vo.BlogFavorVO;
import com.sky.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "博客详情页面")
@RequestMapping("/detail")
public class BlogDetailController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private FavorService favorService;

    @Autowired
    private CommentService commentService;

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

    @ApiOperation(value = "按博客id获取评论")
    @GetMapping("/comment")
    public Result<List<CommentVO>> getComments(@ApiParam(value = "博客id", required = true) @Valid @RequestParam Long postId,
                                               @ApiParam(value = "查询类型: 1最新 2最热") @Valid @RequestParam Integer flag) {
        List<CommentVO> vos = commentService.listComment(postId, flag);
        return Result.success(vos);
    }

    @ApiOperation(value = "发一级评论, 需要携带token访问")
    @PostMapping("/comment")
    public Result<String> createComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return commentService.saveComment(commentDTO, Long.valueOf(userId)) > 0 ? Result.success() : Result.error("评论失败");
    }

    @ApiOperation(value = "发二级评论或其子评论, 需要携带token访问")
    @PostMapping("/comment2")
    public Result<String> createComment2(@RequestBody Comment2DTO comment2DTO, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return commentService.saveMoreComment(comment2DTO, Long.valueOf(userId)) > 0 ? Result.success() : Result.error("评论失败");
    }
}
