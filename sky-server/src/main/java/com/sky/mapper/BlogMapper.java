package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Blog;
import com.sky.vo.BlogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    List<BlogVO> selectBlogDECSByLikes(@Param("categoryId") Long categoryId, @Param("userId") Long userId);

    List<BlogVO> selectBlogForLike(@Param("userId") Long userId, @Param("postId") Long postId);
}
