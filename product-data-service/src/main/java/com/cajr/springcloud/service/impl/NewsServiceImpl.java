package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.mapper.NewsMapper;
import com.cajr.springcloud.service.NewsLogsService;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.vo.News;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:03
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Resource
    NewsMapper newsMapper;

    @Resource
    NewsLogsService newsLogsService;

    @Override
    public List<News> findAll() {
        return newsMapper.findAll();
    }

    @Override
    public List<News> findSection() {
        return this.newsMapper.findSectionNewsById(newsLogsService.hotNewsIdList());
    }

    @Override
    public List<News> findAllTimeDesc() {
        return this.newsMapper.findAllTimeDesc();
    }

    @Override
    public News getOne(long id) {
        return newsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int add(News news) {
        return this.newsMapper.insertSelective(news);
    }
}
