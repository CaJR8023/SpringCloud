package com.cajr.springcloud.mapper;

import com.cajr.springcloud.vo.Recommendations;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:01
 */

public interface RecommendationsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Recommendations record);

    int insertSelective(Recommendations record);

    Recommendations selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Recommendations record);

    int updateByPrimaryKey(Recommendations record);

    List<Recommendations> findAllByUserId(Long userId);
}