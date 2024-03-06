package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.BlogDTO;
import com.sky.entity.Blog;

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
}
