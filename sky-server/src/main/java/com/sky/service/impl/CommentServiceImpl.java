package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.Comment2DTO;
import com.sky.dto.CommentDTO;
import com.sky.entity.Comment;
import com.sky.entity.User;
import com.sky.exception.IncorrectParameterException;
import com.sky.exception.ObjectNullException;
import com.sky.mapper.CommentMapper;
import com.sky.service.CommentService;
import com.sky.service.UserService;
import com.sky.vo.CommentVO;
import com.sky.vo.CommentVO2;
import com.sky.vo.NoticeCommentVO;
import com.sky.vo.NoticeTotalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    /**
     * 按博客查询评论列表
     * @return
     */
    @Override
    public List<CommentVO> listComment(Long postId, Integer flag) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId)
                .eq(Comment::getRootCommentId, 0)
                .eq(Comment::getStatus, 0);
        if (flag == 1) {
            wrapper.orderByDesc(Comment::getCreateTime);
        }
        List<Comment> comments = baseMapper.selectList(wrapper);
        List<CommentVO> vos = comments.stream()
                .map(comment -> {
                    CommentVO vo = CommentVO.builder()
                            .id(comment.getId())
                            .postId(comment.getPostId())
                            .userId(comment.getUserId())
                            .userName(comment.getUserName())
                            .content(comment.getContent())
                            .avatar(comment.getAvatar())
                            .parentId(comment.getParentId())
                            .parentUserName(comment.getParentUserName())
                            .rootCommentId(comment.getRootCommentId())
                            .createTime(comment.getCreateTime())
                            .status(comment.getStatus())
                            .build();
                    LambdaQueryWrapper<Comment> wrapper1 = new LambdaQueryWrapper<>();
                    wrapper1.eq(Comment::getPostId, postId)
                            .eq(Comment::getRootCommentId, comment.getId())
                            .eq(Comment::getStatus, 0)
                            .orderByDesc(Comment::getUpdateTime);
                    List<Comment> list = baseMapper.selectList(wrapper1);
                    vo.setTotal(list.size());
                    vo.setReplyComments(list.stream()
                            .map(comment2 -> {
                                CommentVO2 vo2 = CommentVO2.builder()
                                        .id(comment2.getId())
                                        .postId(comment2.getPostId())
                                        .userId(comment2.getUserId())
                                        .userName(comment2.getUserName())
                                        .content(comment2.getContent())
                                        .avatar(comment2.getAvatar())
                                        .parentId(comment2.getParentId())
                                        .parentUserName(comment2.getParentUserName())
                                        .rootCommentId(comment2.getRootCommentId())
                                        .createTime(comment2.getCreateTime())
                                        .flag1(0)
                                        .flag2(0)
                                        .build();
                                if (Objects.equals(comment2.getUserId(), comment.getUserId())) {
                                    vo2.setFlag1(1);
                                }
                                if (Objects.equals(comment2.getParentId(), comment.getId())) {
                                    vo2.setFlag2(1);
                                }
                                return vo2;
                            }).collect(Collectors.toList()));
                    return vo;
                }).collect(Collectors.toList());
        if (flag == 2) {
            return vos.stream().sorted(Comparator.comparing(CommentVO::getTotal).reversed()).collect(Collectors.toList());
        }
        return vos;
    }

    /**
     * 保存一级评论
     * @param commentDTO
     * @return
     */
    @Override
    public int saveComment(CommentDTO commentDTO, Long userId) {
        User user = userService.getUserById(userId);
        Comment comment = Comment.builder()
                .postId(commentDTO.getPostId())
                .userId(userId)
                .userName(user.getUserName())
                .content(commentDTO.getContent())
                .avatar(user.getAvatar())
                .build();
        return baseMapper.insert(comment);
    }

    /**
     * 保存二级及其子评论
     * @param comment2DTO
     * @param userId
     * @return
     */
    @Override
    public int saveMoreComment(Comment2DTO comment2DTO, Long userId) {
        User user = userService.getUserById(userId);
        User userById = selectUserByCommentId(comment2DTO.getParentId());
        Comment comment = Comment.builder()
                .postId(comment2DTO.getPostId())
                .userId(userId)
                .userName(user.getUserName())
                .content(comment2DTO.getContent())
                .avatar(user.getAvatar())
                .parentId(comment2DTO.getParentId())
                .parentUserName(userById.getUserName())
                .rootCommentId(comment2DTO.getRootCommentId())
                .build();
        return baseMapper.insert(comment);
    }

    /**
     * 按照评论id查找发表的用户
     * @param commentId
     * @return
     */
    @Override
    public User selectUserByCommentId(Long commentId) {
        Comment comment = baseMapper.selectById(commentId);
        return userService.getUserById(comment.getUserId());
    }

    /**
     * 按id查讯博客评论条数
     * @param postId
     * @return
     */
    @Override
    public Long countComment(Long postId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId);
        Long count = baseMapper.selectCount(wrapper);
        return count;
    }

    /**
     * 查询所有有效的评论
     * @return
     */
    @Override
    public List<Comment> getAllCommentAlives() {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getStatus, 0);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 修改评论状态
     * @param commentId
     * @param status
     * @return
     */
    @Override
    public boolean modifyComments(Integer commentId, Integer status) {
        if (baseMapper.selectById(commentId) == null) {
            throw new ObjectNullException(MessageConstant.PRODUCT_NULL);
        }
        if (status != 1 && status != 0) {
            throw new IncorrectParameterException(MessageConstant.INPUT_LIKE_ERROR);
        }
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, commentId)
                .set(Comment::getStatus, status);
        return baseMapper.update(null, wrapper) > 0;
    }

    /**
     * 获取通知中的评论
     * @param userId
     * @return
     */
    @Override
    public List<NoticeCommentVO> getNewComment(Long userId) {
        List<NoticeCommentVO> list1 = baseMapper.selectNoticeCommentsBlog(userId);
        List<NoticeCommentVO> list2 = baseMapper.selectNoticeCommentsComment(userId);
        List<NoticeCommentVO> vos = Stream.concat(list1.stream(), list2.stream())
                .sorted(Comparator.comparing(NoticeCommentVO::getUpdateTime).reversed())
                .collect(Collectors.toList());
        return vos;
    }
}
