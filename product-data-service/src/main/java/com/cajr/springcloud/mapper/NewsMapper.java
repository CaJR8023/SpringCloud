package com.cajr.springcloud.mapper;

import com.cajr.springcloud.vo.News;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:00
 */

public interface NewsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(News record);

    int insertSelective(News record);

    News selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKey(News record);

    List<News> findAll();

    List<News> findSectionNews(List<Long> userIds);

    List<News> findSectionNewsById(List<Long> ids);
}