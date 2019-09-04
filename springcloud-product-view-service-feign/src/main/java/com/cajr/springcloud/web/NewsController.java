package com.cajr.springcloud.web;

import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.vo.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
public class NewsController {
    @Autowired
    NewsService newsService;

    @Value("${version}")
    String version;

    @RequestMapping("/news")
    public List<News> products(Model model){
        return newsService.listNews();
    }
}
