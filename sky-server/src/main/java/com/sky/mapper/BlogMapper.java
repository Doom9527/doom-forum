package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Blog;
import com.sky.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    /**
     * 主页面按分类查找,点赞数降序排序博客
     * @param categoryId
     * @param userId
     * @return
     */
    List<BlogVO> selectBlogDECSByLikes(@Param("categoryId") Long categoryId, @Param("userId") Long userId);

    /**
     * 主页点赞返回VO
     * @param userId
     * @param postId
     * @return
     */
    List<BlogVO> selectBlogForLike(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 详情页博客VO
     */
    List<BlogDetailVO> selectBlogDetail(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 查看收藏的博客
     */
    List<BlogFavorVO> selectBlogDECSByDateTime(@Param("userId") Long userId);

    /**
     * 查看发过的博客
     * @param userId
     * @return
     */
    List<BlogSentVO> selectBlogSent(Long userId);

}
