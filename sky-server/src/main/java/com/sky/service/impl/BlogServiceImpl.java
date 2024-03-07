package com.sky.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.MQInfo.BlogPublishInfo;
import com.sky.constant.RabbitMQConstant;
import com.sky.dto.BlogDTO;
import com.sky.entity.Blog;
import com.sky.mapper.BlogMapper;
import com.sky.service.BlogService;
import com.sky.service.OssService;
import com.sky.vo.BlogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OssService ossService;

    /**
     * 发布博客
     * @param blogDTO
     * @return
     */
    @Override
    public boolean publishBlog(BlogDTO blogDTO) {
        BlogPublishInfo blogPublishInfo = BlogPublishInfo.builder()
                .title(blogDTO.getTitle())
                .content(blogDTO.getContent())
                .categoryId(blogDTO.getCategoryId())
                .authorId(blogDTO.getAuthorId())
                .build();
        String Json = JSONUtil.toJsonStr(blogPublishInfo);
        rabbitTemplate.convertAndSend(RabbitMQConstant.BLOG_PUBLISH_EXCHANGE, RabbitMQConstant.BLOG_PUBLISH_KEY, Json);

        String url = ossService.uploadFileBlogPic(blogDTO.getPicture());
        updateURL(url, blogPublishInfo.getAuthorId());
        log.info("用户id: "+blogPublishInfo.getAuthorId() + " 存入url: " + url);
        return true;
    }

    @Override
    public Integer updateMySQL(String title, String content, Long categoryId, Long authorId) {
        Blog blog = Blog.builder()
                .title(title)
                .content(content)
                .categoryId(categoryId)
                .authorId(authorId).build();
        return baseMapper.insert(blog);
    }

    /**
     * 更新博客图片url
     * @param url
     * @param authorId
     * @return
     */
    @Override
    public boolean updateURL(String url, Long authorId) {
        LambdaUpdateWrapper<Blog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Blog::getPicture, url)
                .eq(Blog::getAuthorId, authorId);
        return baseMapper.update(null, wrapper) > 0;
    }

    /**
     * 主页面获取博客
     * @param categoryId
     * @return
     */
    @Override
    public List<BlogVO> getBlogByCategoryId(Long categoryId, Long userId) {
        List<BlogVO> vos = baseMapper.selectBlogDECSByLikes(categoryId, userId);
        return vos;
    }
}
