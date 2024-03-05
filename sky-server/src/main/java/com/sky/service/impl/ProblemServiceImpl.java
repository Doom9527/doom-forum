package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Problem;
import com.sky.mapper.ProblemMapper;
import com.sky.service.ProblemService;
import com.sky.vo.ProblemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {
    /**
     * 获取密保问题
     * @return
     */
    @Override
    public List<ProblemVO> getProblems() {
        List<Problem> problems = baseMapper.selectList(null);
        List<ProblemVO> vos = problems.stream()
                .map(problem -> {
                    ProblemVO vo = ProblemVO.builder()
                            .id(problem.getId())
                            .name(problem.getName())
                            .build();
                    return vo;
                }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public Problem getProblemById(Long id) {
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Problem::getId, id);
        return baseMapper.selectOne(wrapper);
    }
}
