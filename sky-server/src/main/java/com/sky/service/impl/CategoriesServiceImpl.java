package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.StatusConstant;
import com.sky.entity.Categories;
import com.sky.mapper.CategoriesMapper;
import com.sky.service.CategoriesService;
import com.sky.vo.CategoriesVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories> implements CategoriesService {
    @Override
    public List<CategoriesVO> getCategories() {
        LambdaQueryWrapper<Categories> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Categories::getStatus, StatusConstant.DISABLE);
        List<Categories> list = baseMapper.selectList(wrapper);
        List<CategoriesVO> vos = list.stream()
                .map(categories -> {
                    CategoriesVO vo = CategoriesVO.builder()
                            .id(categories.getId())
                            .name(categories.getName())
                            .description(categories.getDescription())
                            .build();
                    return vo;
                }).collect(Collectors.toList());
        return vos;
    }
}
