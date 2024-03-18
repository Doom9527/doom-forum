package com.sky.controller.web;

import com.sky.result.Result;
import com.sky.service.BlogService;
import com.sky.service.CommentService;
import com.sky.service.FollowService;
import com.sky.utils.JwtUtils;
import com.sky.vo.NoticeCommentVO;
import com.sky.vo.NoticeTotalVO;
import com.sky.vo.UserFollowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@Api(tags = "通知模块")
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private FollowService followService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查看新增关注, 携带token")
    @GetMapping("/follow")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<List<UserFollowVO>> getUserFans(HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        List<UserFollowVO> vos = followService.selectFansById(Long.valueOf(id), Long.valueOf(id));
        return Result.success(vos);
    }

    @ApiOperation(value = "查看赞和收藏, 携带token")
    @GetMapping("/total")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<List<NoticeTotalVO>> getTotal(HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        List<NoticeTotalVO> vos = blogService.getNewLikeAndFavor(Long.valueOf(id));
        return Result.success(vos);
    }

    @ApiOperation(value = "查看新增评论, 携带token")
    @GetMapping("/comment")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<List<NoticeCommentVO>> getComment(HttpServletRequest request) {
        String id = JwtUtils.getUserId(request.getHeader("token"));
        List<NoticeCommentVO> vos = commentService.getNewComment(Long.valueOf(id));
        return Result.success(vos);
    }
}
