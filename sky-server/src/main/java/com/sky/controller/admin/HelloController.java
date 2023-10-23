package com.sky.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;


@Slf4j
@RestController
public class HelloController {

    @Resource
    private UserService userService;

    @RequestMapping("/hell2")
    @PreAuthorize("hasAuthority('aa')")
    public String hello2(){
        //List<String> list = menuMapper.selectPermsByUserId(2L);
        //System.out.println(list);
        return "a";
    }

    @RequestMapping("/a1")
    @PreAuthorize("hasAuthority('system:test:list')")
    public String a1(){
        log.warn("b1");
        return "a1";
    }

    @RequestMapping("/hell3")
    public Result<List<User>> hello3() throws JsonProcessingException {
        return Result.success(userService.getAllUsers());
    }


}
