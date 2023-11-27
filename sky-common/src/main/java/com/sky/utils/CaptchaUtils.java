package com.sky.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.sky.constant.MessageConstant;
import com.sky.exception.LoginFailedException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/*
   包括验证码工具类， 获取用户ip工具类
 */
@Slf4j
public class CaptchaUtils {

    public static String setCaptcha (HttpServletResponse response) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(250, 100, 4, 4);

        //写入浏览器
        captcha.write(response.getOutputStream());

        //关闭流
        response.getOutputStream().close();

        return captcha.getCode();
    }

    public static boolean verifyCaptcha (String code , Object redisCode) {
        if (Objects.isNull(redisCode)) {
            throw new LoginFailedException(MessageConstant.VERIFY_CODE_TIMEOUT);
        }
        log.warn(code);
        log.warn(redisCode.toString());
        return code.equalsIgnoreCase(redisCode.toString());
    }


}
