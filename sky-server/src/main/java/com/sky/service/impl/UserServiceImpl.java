package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.PageDTO;
import com.sky.entity.Problem;
import com.sky.entity.User;
import com.sky.exception.ObjectNullException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.UserMapper;
import com.sky.result.PageQuery;
import com.sky.service.ProblemService;
import com.sky.service.UserService;
import com.sky.utils.RedisCache;
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

    @Override
    public List<User> getAllUsers() {
        //return userMapper.getAllUsers();
        if (baseMapper.selectList(null).isEmpty()) {
            throw new ObjectNullException(MessageConstant.USER_IS_NULL);
        }
        return baseMapper.selectList(null);
    }


    @Override
    public User getUserByUserName(String userName) {
        //return userMapper.getUserByUserName(userName);
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName);
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
        wrapper.eq(User::getId, id);
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
}
