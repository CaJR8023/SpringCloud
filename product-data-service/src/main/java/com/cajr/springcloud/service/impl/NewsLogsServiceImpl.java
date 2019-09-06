package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.mapper.NewslogsMapper;
import com.cajr.springcloud.service.NewsLogsService;
import com.cajr.springcloud.vo.Newslogs;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 19:01
 */
@Service
public class NewsLogsServiceImpl implements NewsLogsService {

    @Resource
    NewslogsMapper newslogsMapper;

    @Override
    public List<Newslogs> findAllByUserId(Long userId) {
        return newslogsMapper.findAllByUserId(userId);
    }

    @Override
    public List<Newslogs> findAll() {
        return newslogsMapper.findAll();
    }
}
