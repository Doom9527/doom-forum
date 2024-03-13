package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.Comment2DTO;
import com.sky.dto.CommentDTO;
import com.sky.entity.Comment;
import com.sky.entity.User;
import com.sky.vo.CommentVO;
import java.util.List;

public interface CommentService extends IService<Comment> {

    /**
     * 按博客查询评论列表
     * @return
     */
    List<CommentVO> listComment(Long postId, Integer flag);

    /**
     * 保存一级评论
     * @param commentDTO
     * @return
     */
    int saveComment(CommentDTO commentDTO, Long userId);

    /**
     * 保存二级及其子评论
     * @param comment2DTO
     * @param userId
     * @return
     */
    int saveMoreComment(Comment2DTO comment2DTO, Long userId);

    /**
     * 按照评论id查找发表的用户
     * @param commentId
     * @return
     */
    User selectUserByCommentId(Long commentId);

    /**
     * 按id查讯博客评论条数
     * @param postId
     * @return
     */
    Long countComment(Long postId);

    /**
     * 查询所有有效的评论
     * @return
     */
    List<Comment> getAllCommentAlives();

    /**
     * 修改评论状态
     * @param commentId
     * @param status
     * @return
     */
    boolean modifyComments(Integer commentId, Integer status);
}
