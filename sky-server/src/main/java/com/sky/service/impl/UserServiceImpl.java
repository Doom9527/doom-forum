package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.PageDTO;
import com.sky.entity.Follow;
import com.sky.entity.Problem;
import com.sky.entity.User;
import com.sky.exception.ObjectNullException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.FollowMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.PageQuery;
import com.sky.service.*;
import com.sky.utils.RedisCache;
import com.sky.vo.UserDetailVO;
import com.sky.vo.UserFollowVO;
import com.sky.vo.UserOPVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private LikesService likesService;

    @Autowired
    private FavorService favorService;

    @Autowired
    private FollowService followService;

    @Override
    public List<User> getAllUsers() {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getStatus, 0);
        //return userMapper.getAllUsers();
        if (baseMapper.selectList(wrapper).isEmpty()) {
            throw new ObjectNullException(MessageConstant.USER_IS_NULL);
        }
        return baseMapper.selectList(wrapper);
    }


    @Override
    public User getUserByUserName(String userName) {
        //return userMapper.getUserByUserName(userName);
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName)
                .eq(User::getStatus, 0);
//        if (baseMapper.selectOne(wrapper) == null) {
//            throw new ObjectNullException(MessageConstant.USER_IS_NULL);
//        }
        return baseMapper.selectOne(wrapper);
    }

    @Override
    @Transactional
    public void InsertUser(User user, Long problem) {
        baseMapper.insert(user);
        User user1 = getUserByUserName(user.getUserName());
        baseMapper.relateUserAndRole(user1.getId());
        baseMapper.relateUserAndProblem(user.getId(), problem);
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Override
    public User getUserById(Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, id)
                .eq(User::getStatus, 0);
//        if (baseMapper.selectOne(wrapper) == null) {
//            throw new ObjectNullException(MessageConstant.USER_IS_NULL);
//        }
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 根据id查询密保问题
     * @param id
     * @return
     */
    @Override
    public Problem getProblemByUserId(String id) {
        User user = getUserById(Long.valueOf(id));
        List<Long> list = baseMapper.findProblemIdsByUserId(user.getId());
        if (list.isEmpty()) {
            throw new ObjectNullException(MessageConstant.PROBLEM_IS_NULL);
        }
        Problem problem = problemService.getProblemById(list.get(0));
        return problem;
    }

    /**
     * 验证密保问题
     * @param answer
     * @return
     */
    @Override
    public Boolean checkAnswer(String answer, String userId) {
        User user = getUserById(Long.valueOf(userId));
        if (user.getAnswer().equals(answer)) {
            // 存入缓存30min
            redisCache.setCacheObject("modifyPassword" + "-" + userId, userId, 30, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    /**
     * 修改密码
     * @param password
     * @param userId
     * @return
     */
    @Override
    public boolean modifyPassword(String password, String userId) {
        //验证redis
        if (redisCache.getCacheObject("modifyPassword" + "-" + userId) == null) {
            throw new ObjectNullException(MessageConstant.MODIFY_REDIS_EXPIRE);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        // 检查密码是否和以前一样
//        User user = getUserById(Long.valueOf(userId));
//        if (password.equals(user.getPassword())) {
//            throw new PasswordErrorException(MessageConstant.PASSWORD_RE_ERROR);
//        }
        //使用BCrypt加密
        password = passwordEncoder.encode(password);
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getPassword, password)
                .eq(User::getId, userId);
        redisCache.deleteObject("modifyPassword" + "-" + userId);
        return baseMapper.update(null, wrapper) > 0;
    }

    /**
     * 添加用户头像url
     * @param url
     * @param id
     * @return
     */
    @Override
    public boolean addAvatarURL(String url, Long id) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getAvatar, url)
                .eq(User::getId, id);
        return baseMapper.update(null, wrapper) > 0;
    }

    /**
     * 查看全部用户
     * @param page
     * @return
     */
    @Override
    public IPage<UserOPVO> selectAll(Page<User> page) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 0);
        Page<User> pages = baseMapper.selectPage(page, wrapper);
        List<UserOPVO> vos = pages.getRecords().stream()
                .map(user -> {
                    UserOPVO vo = UserOPVO.builder()
                            .id(user.getId())
                            .userName(user.getUserName())
                            .status(user.getStatus())
                            .avatar(user.getAvatar())
                            .userType(user.getUserType())
                            .createTime(user.getCreateTime())
                            .updateTime(user.getUpdateTime()).build();
                    return vo;
                }).collect(Collectors.toList());

        IPage<UserOPVO> resultPage = new Page<>(page.getCurrent(), page.getSize());
        resultPage.setCurrent(pages.getCurrent());
        resultPage.setTotal(pages.getTotal());
        resultPage.setRecords(vos);

        return resultPage;
    }

    /**
     * 查看关注列表
     * @param userId
     * @return
     */
    @Override
    public List<UserFollowVO> getUserFollowed(String userId) {
        List<UserFollowVO> vos = baseMapper.selectUserForFollow(Long.valueOf(userId));
        return vos;
    }

    /**
     * 获取用户详情
     * @param userId
     * @return
     */
    @Override
    public UserDetailVO getUserDetail(Long userId, Long id) {
        User user = getUserById(userId);
        if (user == null) {
            throw new ObjectNullException(MessageConstant.USER_IS_NULL);
        }
        Long follows = followService.countFollowsById(String.valueOf(userId));
        Long fans = followService.countFansById(String.valueOf(userId));
        int totals = baseMapper.selectUserTotalCount(userId);
        Follow follow = followService.selectFollowByDuoId(id, userId);
        UserDetailVO vo = UserDetailVO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .avatar(user.getAvatar())
                .followCount(follows)
                .fansCount(fans)
                .totalCount(totals)
                .status(0).build();
        if (follow != null) {
            vo.setStatus(1);
        }
        return vo;
    }
}
