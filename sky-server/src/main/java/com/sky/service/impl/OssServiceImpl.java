package com.sky.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sky.service.OssService;
import com.sky.utils.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@Service
public class OssServiceImpl implements OssService {

    // 上传头像到oss
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
            // 关闭oss
            ossClient.shutdown();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
