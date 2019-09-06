package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.mapper.RecommendationsMapper;
import com.cajr.springcloud.service.RecommendationsService;
import com.cajr.springcloud.vo.Recommendations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 18:09
 */
@Service
public class RecommendationsServiceImpl implements RecommendationsService {
    @Resource
    RecommendationsMapper recommendationsMapper;

    @Override
    public List<Recommendations> findAllByUserId(Long userId) {
        return recommendationsMapper.findAllByUserId(userId);
    }
}
