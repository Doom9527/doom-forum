package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Categories;
import com.sky.vo.CategoriesVO;

import java.util.List;

public interface CategoriesService extends IService<Categories> {
    List<CategoriesVO> getCategories();
}
