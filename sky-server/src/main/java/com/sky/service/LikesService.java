package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.BlogLikeDTO;
import com.sky.entity.Likes;
import com.sky.vo.BlogDetailVO;
import com.sky.vo.BlogVO;

import java.util.List;

public interface LikesService extends IService<Likes> {

    /**
     * 点赞或取消点赞
     * @param userId
     * @return
     */
    BlogVO likeBlog(BlogLikeDTO blogLikeDTO, Long userId);

    /**
     * 点赞
     * @param blogLikeDTO
     * @param userId
     * @return
     */
    BlogDetailVO likeBlogDetail(BlogLikeDTO blogLikeDTO, Long userId);

    /**
     * 按博客id和用户id查找点赞记录
     * @param postId
     * @param userId
     * @return
     */
    List<Likes> selectBlogByDuoId(Long postId, Long userId);
}
