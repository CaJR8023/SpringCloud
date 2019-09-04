package com.cajr.springcloud.service;

import com.cajr.springcloud.vo.News;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:02
 */
public interface NewsService {
    List<News> findAll();

    News getOne(long id);
}
