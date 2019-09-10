package com.cajr.springcloud.web;

import com.cajr.springcloud.client.ClientFeign;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Recommendations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RefreshScope
public class NewsController {
    @Autowired
    NewsService newsService;

    @Resource
    ClientFeign clientFeign;

    @Value("${version}")
    String version;

    @GetMapping("/news")
    public List<News> products(Model model){
        return newsService.listNews();
    }


    @GetMapping("/recommendations")
    public List<Recommendations> recommendationsList(){
        Long userId = 1L;
        return this.clientFeign.listRecommendations(userId);
    }
}
