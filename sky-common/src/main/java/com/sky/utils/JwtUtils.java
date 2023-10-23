package com.sky.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sky.constant.MessageConstant;
import com.sky.exception.JWTNullException;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    //30分钟后过期
    //private static final long EXPIRE_TIME = 1*1000;
    private static final long EXPIRE_TIME = 30*60*1000;
    //jwt密钥
    private static final String TOKEN_SECRET = "ljdyaishijin**3nkjkj??";

    /**
     * 生成jwt字符串，在5分钟后过期
     * @param userId
     * @param info
     * @return
     */
    public static String sign(String userId, Map<String , Object> info){
        try{
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            return JWT.create()
                    //将ID保存到token里面
                    .withAudience(userId)
                    //存放自定义数据
                    .withClaim("info",info)
                    //30分钟后token过期
                    .withExpiresAt(date)
                    //token密钥
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据token获取用户id
     * @param token
     * @return
     */
    public static String getUserId(String token){
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            return userId;
        }catch (JWTDecodeException e){
            return null;
        }
    }



    /**
     * 根据token获取自定义数据info
     * @param token
     * @return
     */
    public static Map<String,Object> getInfo(String token){
        try {
            return JWT.decode(token).getClaim("info").asMap();
        }catch (JWTDecodeException e){
            return null;
        }
    }

    /**
     * 校验 token
     * @param token
     * @return
     */
    public static boolean checkSign(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        }catch (JWTVerificationException e){
            throw new JWTNullException(MessageConstant.TOKEN_CHECKFAIL);
        }
    }
}
