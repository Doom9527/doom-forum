package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserFollowDTO;
import com.sky.entity.Follow;
import com.sky.exception.IncorrectParameterException;
import com.sky.mapper.FollowMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.FollowService;
import com.sky.vo.UserFollowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Autowired
    private UserMapper userMapper;

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
     * 查询粉丝数
     * @param userId
     * @return
     */
    @Override
    public Long countFansById(String userId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowUserId, userId)
                .eq(Follow::getStatus, 1);
        return baseMapper.selectCount(wrapper);
    }

    /**
     * 查询关注数
     * @param userId
     * @return
     */
    @Override
    public Long countFollowsById(String userId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
                .eq(Follow::getStatus, 1);
        return baseMapper.selectCount(wrapper);
    }

    /**
     * 按两个id查询收藏记录
     * @param userId
     * @param userFollowId
     * @return
     */
    @Override
    public Follow selectFollowByDuoId(Long userId, Long userFollowId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, userFollowId);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 按id查询该用户粉丝
     * @param userId
     * @return
     */
    @Override
    public List<UserFollowVO> selectFansById(Long fansId, Long userId) {
        List<UserFollowVO> vos = userMapper.getFansByUserId(fansId, userId);
        return vos;
    }

    @Override
    public Follow selectFollowIf(Long userId, Long userFollowId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, userFollowId)
                .eq(Follow::getStatus, 1);
        return baseMapper.selectOne(wrapper);
    }
}
