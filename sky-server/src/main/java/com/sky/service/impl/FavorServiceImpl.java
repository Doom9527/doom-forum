package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.BlogFavorDTO;
import com.sky.entity.Favor;
import com.sky.exception.IncorrectParameterException;
import com.sky.exception.ObjectNullException;
import com.sky.mapper.FavorMapper;
import com.sky.service.BlogService;
import com.sky.service.FavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavorServiceImpl extends ServiceImpl<FavorMapper, Favor> implements FavorService {

    @Autowired
    private BlogService blogService;

    /**
     * 收藏
     * @param blogFavorDTO
     * @param userId
     * @return
     */
    @Override
    public boolean favorBlogDetail(BlogFavorDTO blogFavorDTO, Long userId) {
        if (blogService.getAliveBlogById(blogFavorDTO.getPostId()) == null) {
            throw new ObjectNullException(MessageConstant.BLOG_NULL);
        }
        //判断是否有收藏记录
        List<Favor> likes = selectBlogByDuoId(blogFavorDTO.getPostId(), userId);
        if (likes.isEmpty()) { // 没有找到已存在的收藏记录
            Favor like = new Favor();
            like.setPostId(blogFavorDTO.getPostId());
            like.setUserId(userId);
            if (blogFavorDTO.getStatus() == 0) {
                // 插入新的收藏记录
                like.setStatus(1);
            } else {
                throw new IncorrectParameterException(MessageConstant.INPUT_LIKE_ERROR);
            }
            baseMapper.insert(like);
        } else { // 找到了已存在的记录
            LambdaUpdateWrapper<Favor> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Favor::getId, likes.get(0).getId());
            if (blogFavorDTO.getStatus() == 0) {
                // 收藏
                wrapper.set(Favor::getStatus, 1);
            } else {
                // 取消收藏
                wrapper.set(Favor::getStatus, 0);
            }
            baseMapper.update(null, wrapper);
        }

        return true;
    }

    /**
     * 按博客id和用户id查找收藏记录
     * @param postId
     * @param userId
     * @return
     */
    @Override
    public List<Favor> selectBlogByDuoId(Long postId, Long userId) {
        LambdaQueryWrapper<Favor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favor::getPostId, postId)
                .eq(Favor::getUserId, userId);
        List<Favor> favors = baseMapper.selectList(wrapper);
        return favors;
    }

    /**
     * 按博客id查询收藏数
     * @param postId
     * @return
     */
    @Override
    public Long selectBlogFavorCount(Long postId) {
        LambdaQueryWrapper<Favor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favor::getPostId, postId)
                .eq(Favor::getStatus, 1);
        Long count = baseMapper.selectCount(wrapper);
        return count;
    }

    /**
     * 查找所有有效的收藏
     * @return
     */
    @Override
    public List<Favor> selectAllFavorsAlive() {
        LambdaQueryWrapper<Favor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favor::getStatus, 1);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Favor selectFavorIf(Long postId, Long userId) {
        LambdaQueryWrapper<Favor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favor::getPostId, postId)
                .eq(Favor::getUserId, userId)
                .eq(Favor::getStatus, 1);
        return baseMapper.selectOne(wrapper);
    }
}
