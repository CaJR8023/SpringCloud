package com.cajr.springcloud.mapper;

import com.cajr.springcloud.vo.Newsmodules;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:01
 */

public interface NewsmodulesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Newsmodules record);

    int insertSelective(Newsmodules record);

    Newsmodules selectByPrimaryKey(Integer id);

    Newsmodules selectByName(String name);

    int updateByPrimaryKeySelective(Newsmodules record);

    int updateByPrimaryKey(Newsmodules record);

    List<Newsmodules> findAll();

}