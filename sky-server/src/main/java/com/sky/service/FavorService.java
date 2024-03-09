package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.BlogFavorDTO;
import com.sky.entity.Favor;

import java.util.List;

public interface FavorService extends IService<Favor> {

    /**
     * 收藏
     * @param blogFavorDTO
     * @param userId
     * @return
     */
    boolean favorBlogDetail(BlogFavorDTO blogFavorDTO, Long userId);

    /**
     * 按博客id和用户id查找收藏记录
     * @param postId
     * @param userId
     * @return
     */
    List<Favor> selectBlogByDuoId(Long postId, Long userId);

    /**
     * 按博客id查询收藏数
     * @param postId
     * @return
     */
    Long selectBlogFavorCount(Long postId);

    /**
     * 查找所有有效的收藏
     * @return
     */
    List<Favor> selectAllFavorsAlive();
}
