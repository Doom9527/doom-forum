package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.MQInfo.BlogPublishInfo;
import com.sky.constant.RabbitMQConstant;
import com.sky.dto.BlogDTO;
import com.sky.dto.BlogHomePageDTO;
import com.sky.dto.BlogPageDTO;
import com.sky.entity.*;
import com.sky.mapper.BlogMapper;
import com.sky.service.*;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private OssService ossService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private FavorService favorService;

    @Autowired
    private FollowService followService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoriesService categoriesService;

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
        String url = ossService.uploadFileBlogPic(blogDTO.getPicture());
        //rabbitTemplate.convertAndSend(RabbitMQConstant.BLOG_PUBLISH_EXCHANGE, RabbitMQConstant.BLOG_PUBLISH_KEY, Json);
        updateMySQL(blogDTO.getTitle(), blogDTO.getContent(), blogDTO.getCategoryId(), blogDTO.getAuthorId(), url);
        log.info("用户id: "+blogPublishInfo.getAuthorId() + " 存入url: " + url);
        return true;
    }

    @Override
    public Integer updateMySQL(String title, String content, Long categoryId, Long authorId, String url) {
        Blog blog = Blog.builder()
                .title(title)
                .content(content)
                .categoryId(categoryId)
                .picture(url)
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
     * @param dto
     * @param userId
     * @return
     */
    @Override
    public List<BlogVO> getBlogByCategoryId(BlogHomePageDTO dto, Long userId) {
        List<BlogVO> vos = baseMapper.selectBlogDECSByLikes(dto.getCategoryId(), userId, dto.getTitle());
        return vos;
    }

    /**
     * 查看博客详情
     * @param blogId
     * @return
     */
    @Override
    public BlogDetailVO getBlogByBlogId(Long userId, Long blogId) {
        List<BlogDetailVO> vos = baseMapper.selectBlogDetail(userId, blogId);
        BlogDetailVO vo = vos.get(0);
        vo.setStatus1(0);
        vo.setStatus2(0);
        vo.setStatus3(0);
        log.info(String.valueOf(vos.size()));
        if (likesService.selectLikesIf(blogId, userId) != null) {
            vo.setStatus1(1);
        }
        if (favorService.selectFavorIf(blogId, userId) != null) {
            vo.setStatus2(1);
        }
        if (followService.selectFollowIf(userId, vo.getAuthorId()) != null) {
            vo.setStatus3(1);
        }
        return vo;
    }

    /**
     * 查看收藏的博客
     * @param userId
     * @return
     */
    @Override
    public List<BlogFavorVO> getBlogForFavor(Long userId) {
        List<BlogFavorVO> vos = baseMapper.selectBlogFavorDECSByDateTime(userId);
        return vos;
    }

    /**
     * 查看点赞的博客
     */
    @Override
    public List<BlogFavorVO> getBlogForLike(Long userId) {
        List<BlogFavorVO> vos = baseMapper.selectBlogLikeDECSByDateTime(userId);
        return vos;
    }

    /**
     * 查看发过的博客
     * @param userId
     * @return
     */
    @Override
    public List<BlogSentVO> getBlogSent(Long userId) {
        List<BlogSentVO> vos = baseMapper.selectBlogSent(userId);
        return vos;
    }

    /**
     * 按id删除博客
     * @param id
     * @return
     */
    @Override
    public boolean deleteBlogById(Long id, Long userId) {
        User user = userService.getUserById(userId);
        LambdaUpdateWrapper<Blog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Blog::getStatus, 1)
                .eq(Blog::getId, id);
        if (user.getUserType().equals("1") || user.getUserType().equals("2")) {
            wrapper.eq(Blog::getAuthorId, userId);
        }
        return baseMapper.update(null, wrapper) > 0;
    }

    /**
     * 管理员查看所有博客
     * @param dto
     * @return
     */
    @Override
    public IPage<BlogOPVO> getAllBlogsOP(Page<Blog> page, BlogPageDTO dto) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Blog::getUpdateTime);
        if (dto.getCategoryId() != null) {
            wrapper.eq(Blog::getCategoryId, dto.getCategoryId());
        }
        if (dto.getUserId() != null) {
            wrapper.eq(Blog::getAuthorId, dto.getUserId());
        }
        if (StrUtil.isNotBlank(dto.getTitle())) {
            wrapper.like(Blog::getTitle, "%" + dto.getTitle() + "%");
        }
        if (dto.getExamine() != null) {
            wrapper.eq(Blog::getExamine, dto.getExamine());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(Blog::getStatus, dto.getStatus());
        }
        Page<Blog> pages = baseMapper.selectPage(page, wrapper);

        List<User> users = userService.getAllUsers();
        List<CategoriesVO> categories = categoriesService.getCategories();

        // 按博客id分类填充点赞、收藏、评论数
        Map<Long, List<Likes>> likesMap = likesService.getAllAliveLikes()
                .stream().collect(Collectors.groupingBy(Likes::getPostId));
        Map<Long, List<Favor>> favorsMap = favorService.selectAllFavorsAlive()
                .stream().collect(Collectors.groupingBy(Favor::getPostId));
        Map<Long, List<Comment>> commentsMap = commentService.getAllCommentAlives()
                .stream().collect(Collectors.groupingBy(Comment::getPostId));

        List<BlogOPVO> vos = pages.getRecords().stream()
                .map(blog -> {
                    BlogOPVO vo = BlogOPVO.builder()
                            .id(blog.getId())
                            .title(blog.getTitle())
                            .content(blog.getContent())
                            .authorId(blog.getAuthorId())
                            .categoryId(blog.getCategoryId())
                            .createTime(blog.getCreateTime())
                            .updateTime(blog.getUpdateTime())
                            .examine(blog.getExamine())
                            .picture(blog.getPicture())
                            .status(blog.getStatus())
                            // 这里使用了Map.getOrDefault(key, default_value)方法来防止postId不存在于Map中时抛出异常，此时返回的是一个空集合，其大小为0
                            .likeCount(likesMap.getOrDefault(blog.getId(), Collections.emptyList()).size())
                            .favorCount(favorsMap.getOrDefault(blog.getId(), Collections.emptyList()).size())
                            .commentCount(commentsMap.getOrDefault(blog.getId(), Collections.emptyList()).size())
                            .build();
                    return vo;
                })
                .peek(vo -> {
                    User user = users.stream()
                            .filter(u -> u.getId().equals(vo.getAuthorId()))
                            .findFirst()
                            .orElse(null);
                    vo.setAuthorName(user.getUserName());
                    vo.setAvatar(user.getAvatar());

                    CategoriesVO category = categories.stream()
                            .filter(c -> c.getId().equals(vo.getCategoryId()))
                            .findFirst()
                            .orElse(null);
                    vo.setCategoryName(category.getName());
                }).collect(Collectors.toList());

        IPage<BlogOPVO> resultPage = new Page<>(page.getCurrent(), page.getSize());
        resultPage.setCurrent(pages.getCurrent());
        resultPage.setTotal(pages.getTotal());
        resultPage.setRecords(vos);

        return resultPage;
    }

    /**
     * 通过博客审核
     * @param ids
     * @return
     */
    @Override
    public Integer passBlogById(Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        LambdaUpdateWrapper<Blog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Blog::getId, list)
                .set(Blog::getExamine, 1);
        return baseMapper.update(null, wrapper);
    }

    /**
     * 批量删除博客
     * @param ids
     * @return
     */
    @Override
    public Integer deleteBlogByIds(Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        LambdaUpdateWrapper<Blog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Blog::getId, list)
                .set(Blog::getStatus, 1);
        return baseMapper.update(null, wrapper);
    }

    /**
     * 批量恢复
     * @param ids
     * @return
     */
    @Override
    public Integer recoverBlogByIds(Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        LambdaUpdateWrapper<Blog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Blog::getId, list)
                .set(Blog::getStatus, 0);
        return baseMapper.update(null, wrapper);
    }

    /**
     * 查看新增赞和收藏
     * @param userId
     * @return
     */
    @Override
    public List<NoticeTotalVO> getNewLikeAndFavor(Long userId) {
        List<NoticeTotalVO> list1 = baseMapper.selectNewLikes(userId);
        List<NoticeTotalVO> list2 = baseMapper.selectNewFavor(userId);
        List<NoticeTotalVO> vos = Stream.concat(list1.stream(), list2.stream())
                .sorted(Comparator.comparing(NoticeTotalVO::getUpdateTime).reversed())
                .collect(Collectors.toList());
        return vos;
    }

    /**
     * 按id查询有效的博客
     * @param postId
     * @return
     */
    @Override
    public Blog getAliveBlogById(Long postId) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getId, postId)
                .eq(Blog::getStatus, 0)
                .eq(Blog::getExamine, 1);
        return baseMapper.selectOne(wrapper);
    }
}
