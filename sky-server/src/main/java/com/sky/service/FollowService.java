package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.UserFollowDTO;
import com.sky.entity.Follow;
import com.sky.vo.UserFollowVO;

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
     * 查询粉丝数
     * @param userId
     * @return
     */
    Long countFansById(String userId);

    /**
     * 查询关注数
     * @param userId
     * @return
     */
    Long countFollowsById(String userId);

    /**
     * 按两个id查询收藏记录
     * @param userId
     * @param userFollowId
     * @return
     */
    Follow selectFollowByDuoId(Long userId, Long userFollowId);

    /**
     * 按id查询该用户粉丝
     * @param userId
     * @return
     */
    List<UserFollowVO> selectFansById(Long fansId, Long userId);
}
