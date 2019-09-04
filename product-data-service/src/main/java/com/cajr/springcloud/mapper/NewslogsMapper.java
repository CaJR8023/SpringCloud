package com.cajr.springcloud.mapper;

import com.cajr.springcloud.vo.Newslogs;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:01
 */

public interface NewslogsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Newslogs record);

    int insertSelective(Newslogs record);

    Newslogs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Newslogs record);

    int updateByPrimaryKey(Newslogs record);

    List<Newslogs> findAllByUserId(Long userId);

    List<Newslogs> findAll();
}