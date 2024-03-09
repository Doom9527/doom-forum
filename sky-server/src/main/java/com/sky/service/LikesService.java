package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.BlogLikeDTO;
import com.sky.entity.Likes;

import java.util.List;

public interface LikesService extends IService<Likes> {

    /**
     * 点赞或取消点赞
     * @param userId
     * @return
     */
    boolean likeBlog(BlogLikeDTO blogLikeDTO, Long userId);

    /**
     * 按博客id和用户id查找点赞记录
     * @param postId
     * @param userId
     * @return
     */
    List<Likes> selectBlogByDuoId(Long postId, Long userId);

    /**
     * 按博客id查询点赞条数
     * @param postId
     * @return
     */
    Long selectLikesCount(Long postId);

    /**
     * 查询所有有效的点赞记录
     * @return
     */
    List<Likes> getAllAliveLikes();
}
