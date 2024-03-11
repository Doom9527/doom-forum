package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.User;
import com.sky.vo.UserFollowVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{id}, 3)")
    void relateUserAndRole (Long id);

    @Insert("INSERT INTO sys_user_problem (user_id, problem_id) values (#{userId}, #{problemId})")
    void relateUserAndProblem(Long userId, Long problemId);

    @Select("SELECT problem_id FROM sys_user_problem WHERE user_id = #{userId}")
    List<Long> findProblemIdsByUserId(Long userId);

    /**
     * 查看关注的用户
     * @param userId
     * @return
     */
    List<UserFollowVO> selectUserForFollow(Long userId);
}
