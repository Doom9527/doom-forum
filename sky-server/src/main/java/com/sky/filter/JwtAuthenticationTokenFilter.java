package com.sky.filter;

import com.sky.constant.MessageConstant;
import com.sky.exception.UserNotLoginException;
import com.sky.testPojo.LoginUser;
import com.sky.utils.BaseContext;
import com.sky.utils.JwtUtils;
import com.sky.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //获取token

        String token = request.getHeader("token");
        System.out.println(request.getRequestURI());
        System.out.println("此处测试是否拿到了token：" + token);
        if (!StringUtils.hasText(token)){
            //放行，后面的过滤器会过滤掉
            filterChain.doFilter(request,response);
            return;
        }
        //验证token
        JwtUtils.checkSign(token);

        //将token存入ThreadLocal中
        BaseContext.setToken(token);

        //获取userId
        String userId = JwtUtils.getUserId(token);
        //从redis中获取用户信息
        String redisKey = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)){
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        }
        //存入SecurityContexHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request,response);
    }
}
