package com.sky.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.service.OssService;
import com.sky.service.UserService;
import com.sky.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl extends ServiceImpl<UserMapper, User> implements OssService {

    @Autowired
    private UserService userService;

    // 注册时上传头像
    @Override
    public String uploadFileAvatarRegister(MultipartFile file, String username) {
        // 从工具类中获取对象
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();
            // 获取文件原始名称，得到图片后缀
            //String[] split = file.getOriginalFilename().split("\\.");
            // 完善、 --> 把文件按照用户分类
            String filename = "avatar/" + username + ".jpg";
            //System.out.println(filename);
            // 调用oss方法实现上传
            // 1、bucketName 2、上传到oss文件路径和文件名称 3、文件的输入流
            ossClient.putObject(bucketName, filename, inputStream);
            // 获取url地址（根据阿里云oss中的图片实例拼接字符串） 拼接url字符串
            // https://edu-leader.oss-cn-beijing.aliyuncs.com/%E4%BB%96.jpg
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            // 关闭oss
            ossClient.shutdown();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 更新头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile file, String username) {
        // 从工具类中获取对象
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();
            // 获取文件原始名称，得到图片后缀
            //String[] split = file.getOriginalFilename().split("\\.");
            // 完善、 --> 把文件按照用户分类
            String filename = "avatar/" + username + ".jpg";
            //System.out.println(filename);
            // 调用oss方法实现上传
            // 1、bucketName 2、上传到oss文件路径和文件名称 3、文件的输入流
            ossClient.putObject(bucketName, filename, inputStream);
            // 获取url地址（根据阿里云oss中的图片实例拼接字符串） 拼接url字符串
            // https://edu-leader.oss-cn-beijing.aliyuncs.com/%E4%BB%96.jpg
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            // 将头像url写入数据库
            userService.addAvatarURL(url, userService.getUserByUserName(username).getId());
            // 关闭oss
            ossClient.shutdown();

            //修改审核状态
            User user = new User();
            user.setId(userService.getUserByUserName(username).getId());
            user.setPhoneStatus(0); // 设置为待审核状态
            this.updateById(user);

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 博客图片上传
     * @param file
     * @return
     */
    @Override
    public String uploadFileBlogPic(MultipartFile file) {
        // 从工具类中获取对象
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();
            // 获取文件原始名称
            String filename = file.getOriginalFilename();
            // 完善1、 --> 在文件名中添加唯一值
            String uuid = UUID.randomUUID().toString().replace("-", "");
            filename = uuid + filename;
            // 完善2、 --> 把文件按照日期分类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            // 拼接时间 yyyy/MM/dd/filename
            filename = datePath + "/" + filename;
            //System.out.println(filename);
            // 调用oss方法实现上传
            // 1、bucketName 2、上传到oss文件路径和文件名称 3、文件的输入流
            ossClient.putObject(bucketName, filename, inputStream);
            // 获取url地址（根据阿里云oss中的图片实例拼接字符串） 拼接url字符串
            // https://edu-leader.oss-cn-beijing.aliyuncs.com/%E4%BB%96.jpg
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            // 关闭oss
            ossClient.shutdown();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
