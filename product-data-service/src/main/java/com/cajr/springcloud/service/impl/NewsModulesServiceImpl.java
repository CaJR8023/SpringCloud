package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.mapper.NewsmodulesMapper;
import com.cajr.springcloud.service.NewsModulesService;
import com.cajr.springcloud.vo.Newsmodules;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 20:17
 */
@Service
public class NewsModulesServiceImpl implements NewsModulesService {
    @Resource
    NewsmodulesMapper newsmodulesMapper;

    @Override
    public List<Newsmodules> list() {
        return this.newsmodulesMapper.findAll();
    }

    @Override
    public Newsmodules selectByName(String name) {
        return newsmodulesMapper.selectByName(name);
    }

    @Override
    public int add(Newsmodules newsmodules) {
        return newsmodulesMapper.insertSelective(newsmodules);
    }
}
