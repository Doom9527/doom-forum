package com.sky.controller.web;

import com.sky.dto.Comment2DTO;
import com.sky.dto.CommentDTO;
import com.sky.result.Result;
import com.sky.service.CommentService;
import com.sky.utils.JwtUtils;
import com.sky.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "评论控制器")
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "按博客id获取评论")
    @GetMapping("/{postId}")
    public Result<List<CommentVO>> getComments(@PathVariable Long postId) {
        List<CommentVO> vos = commentService.listComment(postId);
        return Result.success(vos);
    }

    @ApiOperation(value = "发一级评论")
    @PostMapping
    public Result<String> createComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return commentService.saveComment(commentDTO, Long.valueOf(userId)) > 0 ? Result.success() : Result.error("评论失败");
    }

    @ApiOperation(value = "发二级评论或其子评论")
    @PostMapping("/2")
    public Result<String> createComment2(@RequestBody Comment2DTO comment2DTO, HttpServletRequest request) {
        String userId = JwtUtils.getUserId(request.getHeader("token"));
        return commentService.saveMoreComment(comment2DTO, Long.valueOf(userId)) > 0 ? Result.success() : Result.error("评论失败");
    }
}
