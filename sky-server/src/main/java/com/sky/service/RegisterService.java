package com.sky.service;

import com.sky.dto.UserRegisterDTO;
import com.sky.vo.ProblemVO;

import java.util.List;

public interface RegisterService {

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    boolean register(UserRegisterDTO userRegisterDTO);
}
