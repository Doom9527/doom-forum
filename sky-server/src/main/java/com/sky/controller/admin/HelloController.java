package com.sky.controller.admin;

import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
public class HelloController {

    @Resource
    private UserService userService;

    @RequestMapping("/a1")
    @PreAuthorize("hasAuthority('system:operation:admin')")
    public String a1(){
        log.warn("b1");
        return "a1";
    }

    @RequestMapping("/a2")
    @PreAuthorize("hasAuthority('system:operation:user')")
    public String a2(){
        log.warn("b2");
        return "a2";
    }

}
