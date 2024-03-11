package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserFollowDTO;
import com.sky.entity.Follow;
import com.sky.entity.Likes;
import com.sky.exception.IncorrectParameterException;
import com.sky.mapper.FollowMapper;
import com.sky.service.FollowService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    /**
     * 关注或取消关注
     * @param userFollowDTO
     * @param userId
     * @return
     */
    @Override
    public boolean createFollow(UserFollowDTO userFollowDTO, Long userId) {
        // 不能关注自己
        if (userFollowDTO.getUserFollowId().equals(userId)) {
            throw new IncorrectParameterException(MessageConstant.INPUT_FOLLOW_ERROR);
        }
        // 判断是否有关注记录
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, userFollowDTO.getUserFollowId());
        List<Follow> follows = baseMapper.selectList(wrapper);
        if (follows.isEmpty()) { // 没有找到已存在的关注记录
            Follow follow = new Follow();
            follow.setFollowUserId(userFollowDTO.getUserFollowId());
            follow.setUserId(userId);
            if (userFollowDTO.getStatus() == 0) {
                // 插入新的关注记录
                follow.setStatus(1);
            } else {
                throw new IncorrectParameterException(MessageConstant.INPUT_LIKE_ERROR);
            }
            baseMapper.insert(follow);
        } else { // 找到了已存在的记录
            LambdaUpdateWrapper<Follow> wrapper1 = new LambdaUpdateWrapper<>();
            wrapper1.eq(Follow::getId, follows.get(0).getId());
            if (userFollowDTO.getStatus() == 0) {
                // 关注
                wrapper1.set(Follow::getStatus, 1);
            } else {
                // 取消收藏
                wrapper1.set(Follow::getStatus, 0);
            }
            baseMapper.update(null, wrapper1);
        }
        return true;
    }

    /**
     * 查询所有有效的关注记录
     * @return
     */
    @Override
    public List<Follow> getAllAliveFollow(String userId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getStatus, 1)
                .eq(Follow::getUserId, userId);
        return baseMapper.selectList(wrapper);
    }
}
