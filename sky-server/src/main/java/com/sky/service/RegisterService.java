package com.sky.service;

import com.sky.dto.UserRegisterDTO;

public interface RegisterService {

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    void register(UserRegisterDTO userRegisterDTO);
}
