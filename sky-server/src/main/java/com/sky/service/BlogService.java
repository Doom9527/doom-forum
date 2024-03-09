package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.BlogDTO;
import com.sky.dto.BlogPageDTO;
import com.sky.entity.Blog;
import com.sky.result.Result;
import com.sky.vo.*;

import java.util.List;

public interface BlogService extends IService<Blog> {
    /**
     * 发布博客
     * @param blogDTO
     * @return
     */
    boolean publishBlog(BlogDTO blogDTO);

    Integer updateMySQL(String title, String content, Long categoryId, Long authorId);

    /**
     * 更新博客图片url
     * @param url
     * @param authorId
     * @return
     */
    boolean updateURL(String url, Long authorId);

    /**
     * 主页面获取博客
     * @param categoryId
     * @return
     */
    List<BlogVO> getBlogByCategoryId(Long categoryId, Long userId);

    /**
     * 查看博客详情
     * @param blogId
     * @return
     */
    BlogDetailVO getBlogByBlogId(Long userId, Long blogId);

    /**
     * 查看收藏的博客
     */
    List<BlogFavorVO> getBlogForFavor(Long userId);

    /**
     * 查看发过的博客
     * @param userId
     * @return
     */
    List<BlogSentVO> getBlogSent(Long userId);

    /**
     * 按id删除博客
     * @param id
     * @return
     */
    boolean deleteBlogById(Long id, Long userId);

    /**
     * 按id审核博客
     * @param id
     * @param flag
     * @return
     */
    boolean examineBlogById(Long id, Integer flag);

    /**
     * 管理员查看所有博客
     * @param dto
     * @return
     */
   IPage<BlogOPVO> getAllBlogsOP(Page<Blog> page, BlogPageDTO dto);
}
