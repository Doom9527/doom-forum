package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.PageDTO;
import com.sky.dto.UserPhoneAuditDTO;
import com.sky.dto.UserUpdatePhoneDTO;
import com.sky.entity.Problem;
import com.sky.entity.User;
import com.sky.result.PageQuery;
import com.sky.vo.MyDetailVO;
import com.sky.vo.UserDetailVO;
import com.sky.vo.UserFollowVO;
import com.sky.vo.UserOPVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 查询所有用户
     * @return
     */
    List<User> getAllUsers();

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 注册，添加用户
     */
    void InsertUser(User user, Long id);

    /**
     * 根据id查询用户
     */
    User getUserById(Long id);

    /**
     * 根据id查询密保问题
     * @param id
     * @return
     */
    Problem getProblemByUserId(String id);

    /**
     * 验证密保问题
     * @param answer
     * @return
     */
    Boolean checkAnswer(String answer, String userId);

    /**
     * 修改密码
     * @param password
     * @param userId
     * @return
     */
    boolean modifyPassword(String password, String userId);

    /**
     * 添加用户头像url
     * @param url
     * @param id
     * @return
     */
    boolean addAvatarURL(String url, Long id);

    /**
     * 查看全部用户
     * @param page
     * @return
     */
    IPage<UserOPVO> selectAll(Page<User> page);

    /**
     * 查看关注列表
     * @param userId
     * @return
     */
    List<UserFollowVO> getUserFollowed(String userId);

    /**
     * 获取用户详情
     * @param userId
     * @return
     */
    UserDetailVO getUserDetail(Long userId, Long id);

    /**
     * 我的详情
     * @return
     */
    MyDetailVO getMyDetail(Long userId);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean deleteUserById(Long id);

    /**
     * 修改手机号
     * @param userUpdatePhoneDTO 手机号信息
     */
    void updatePhone(UserUpdatePhoneDTO userUpdatePhoneDTO, String token);

    /**
     * 审核用户手机号
     * @param userPhoneAuditDTO 审核信息
     */
    void auditPhone(UserPhoneAuditDTO userPhoneAuditDTO);
}
