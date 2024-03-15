package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Comment;
import com.sky.vo.NoticeCommentVO;
import com.sky.vo.NoticeTotalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 查看博客回复评论
     * @param userId
     * @return
     */
    List<NoticeCommentVO> selectNoticeCommentsBlog(@Param("userId") Long userId);

    /**
     * 查看评论回复评论
     * @param userId
     * @return
     */
    List<NoticeCommentVO> selectNoticeCommentsComment(@Param("userId") Long userId);
}
