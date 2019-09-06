package com.cajr.springcloud.service;

import com.cajr.springcloud.vo.Recommendations;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 18:04
 */
public interface RecommendationsService {
    List<Recommendations> findAllByUserId(Long userId);
}
