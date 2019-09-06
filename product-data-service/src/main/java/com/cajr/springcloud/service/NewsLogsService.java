package com.cajr.springcloud.service;

import com.cajr.springcloud.vo.Newslogs;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 18:05
 */
public interface NewsLogsService {
    List<Newslogs> findAllByUserId(Long userId);

    List<Newslogs> findAll();
}
