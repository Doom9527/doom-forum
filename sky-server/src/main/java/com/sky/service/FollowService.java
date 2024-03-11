package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.UserFollowDTO;
import com.sky.entity.Follow;
import com.sky.entity.Likes;

import java.util.List;

public interface FollowService extends IService<Follow> {

    /**
     * 关注或取消关注
     * @param userFollowDTO
     * @param userId
     * @return
     */
    boolean createFollow(UserFollowDTO userFollowDTO, Long userId);

    /**
     * 查询所有有效的关注记录
     * @return
     */
    List<Follow> getAllAliveFollow(String userId);
}
