package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Problem;
import com.sky.vo.ProblemVO;

import java.util.List;

public interface ProblemService extends IService<Problem> {
    /**
     * 获取密保问题
     * @return
     */
    List<ProblemVO> getProblems();
}
