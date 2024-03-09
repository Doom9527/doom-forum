package com.sky.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.BlogLikeDTO;
import com.sky.entity.Likes;
import com.sky.exception.IncorrectParameterException;
import com.sky.mapper.LikesMapper;
import com.sky.service.LikesService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements LikesService {

    /**
     * 主界面点赞或取消点赞
     * @param userId
     * @return
     */
    @Override
    public boolean likeBlog(BlogLikeDTO blogLikeDTO, Long userId) {
        //判断是否有点赞记录
        List<Likes> likes = selectBlogByDuoId(blogLikeDTO.getPostId(), userId);
        if (likes.isEmpty()) { // 没有找到已存在的点赞记录
            Likes like = new Likes();
            like.setPostId(blogLikeDTO.getPostId());
            like.setUserId(userId);
            if (blogLikeDTO.getStatus() == 0) {
                // 插入新的点赞记录
                like.setStatus(1);
            } else {
                throw new IncorrectParameterException(MessageConstant.INPUT_LIKE_ERROR);
            }
            baseMapper.insert(like);
        } else { // 找到了已存在的记录
            LambdaUpdateWrapper<Likes> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Likes::getId, likes.get(0).getId());
            if (blogLikeDTO.getStatus() == 0) {
                // 点赞
                wrapper.set(Likes::getStatus, 1);
            } else {
                // 取消点赞
                wrapper.set(Likes::getStatus, 0);
            }
            baseMapper.update(null, wrapper);
        }
        return true;
    }

    /**
     * 按博客id和用户id查找点赞记录
     * @param postId
     * @param userId
     * @return
     */
    @Override
    public List<Likes> selectBlogByDuoId(Long postId, Long userId) {
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getPostId, postId)
                .eq(Likes::getUserId, userId);
        List<Likes> likes = baseMapper.selectList(wrapper);
        return likes;
    }

    /**
     * 按博客id查询点赞条数
     * @param postId
     * @return
     */
    @Override
    public Long selectLikesCount(Long postId) {
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getPostId, postId)
                .eq(Likes::getStatus, 1);
        Long counts = baseMapper.selectCount(wrapper);
        return counts;
    }

    /**
     * 查询所有有效的点赞记录
     * @return
     */
    @Override
    public List<Likes> getAllAliveLikes() {
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getStatus, 1);
        return baseMapper.selectList(wrapper);
    }
}
