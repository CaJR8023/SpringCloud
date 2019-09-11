package com.cajr.springcloud.web;

import com.cajr.springcloud.client.ClientFeign;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Newslogs;
import com.cajr.springcloud.vo.Recommendations;
import com.cajr.springcloud.vo.Users;
import com.cajr.springcloud.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
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


    @GetMapping("/recommendations")
    public List<Recommendations> recommendationsList(){
        Long userId = 1L;
        return this.clientFeign.listRecommendations(userId);
    }

    @GetMapping("/hot")
    public List<News> listHotNews(){
        return clientFeign.listHotNews().subList(1,clientFeign.listHotNews().size());
    }

    @GetMapping("/hot_best")
    public News listBestHotNews(){
        return clientFeign.listHotNews().get(0);
    }

    @GetMapping("/news")
    public News getOneNews(@RequestParam("newsId") Long newsId){
        return clientFeign.getOneNews(newsId);
    }

    @PostMapping("/logs")
    public Object addUserLogs(@RequestParam("newsId") Long newsId,HttpSession session){
        Long userId = 1L;
        Newslogs newslogs = new Newslogs();
        newslogs.setNewsId(newsId);
        newslogs.setUserId(userId);
        newslogs.setViewTime(new Date());
        System.out.println(newslogs.toString());
        return clientFeign.addUserNewsLogs(newslogs);
    }

    @GetMapping("/real_login")
    public String login(@RequestParam("name") String name, HttpSession session){
        Users users1 = this.clientFeign.getOneUserByName(name);
        if (users1 != null){
            session.setAttribute("userId",users1.getId());
            System.out.println(users1.getId());
            return "success";
        }else {
            return "fail";
        }

    }
}
