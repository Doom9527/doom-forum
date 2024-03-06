package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Likes;
import com.sky.mapper.LikesMapper;
import com.sky.service.LikesService;
import org.springframework.stereotype.Service;

@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements LikesService {
}
