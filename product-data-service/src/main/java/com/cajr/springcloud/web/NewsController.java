package com.cajr.springcloud.web;

import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.vo.News;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:05
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    NewsService newsService;

    @GetMapping("/")
    public List<News> findAll(){
        List<News> news = newsService.findAll();
        return newsService.findAll();
    }
    @GetMapping("/getone")
    public News findOne(){
        long id = 85;
        return newsService.getOne(id);
    }
}
