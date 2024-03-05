package com.sky.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    /**
     * 上传头像
     * @param file
     * @param username
     * @return
     */
    String uploadFileAvatarRegister(MultipartFile file, String username);

    /**
     * 更新头像
     * @param file
     * @param username
     * @return
     */
    String uploadFileAvatar(MultipartFile file, String username);
}
