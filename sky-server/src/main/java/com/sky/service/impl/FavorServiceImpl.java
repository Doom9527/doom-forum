package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Favor;
import com.sky.mapper.FavorMapper;
import com.sky.service.FavorService;
import org.springframework.stereotype.Service;

@Service
public class FavorServiceImpl extends ServiceImpl<FavorMapper, Favor> implements FavorService {
}
