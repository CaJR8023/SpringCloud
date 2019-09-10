package com.cajr.springcloud.web;

import com.cajr.springcloud.main.JobSetter;
import com.cajr.springcloud.service.NewsLogsService;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.service.RecommendAlgorithm;
import com.cajr.springcloud.util.NewsImportUtil;
import com.cajr.springcloud.util.NewsScraperUtil;
import com.cajr.springcloud.vo.News;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @Resource
    NewsScraperUtil newsScraperUtil;
    @Resource
    JobSetter jobSetter;
    @Resource
    NewsLogsService newsLogsService;
    @Resource
    @Qualifier("ContentBasedRecommender")
    RecommendAlgorithm recommendAlgorithm;

    @Autowired
    NewsImportUtil newsImportUtil;

//    @Resource
//    @Qualifier("ContentBasedRecommender")
//    RecommendAlgorithm recommendAlgorithm;

    @GetMapping("/")
    public List<News> findAll(){
        List<News> news = newsService.findAll();
        return newsService.findAll();
    }

    @GetMapping("/import")
    public Object findOne() throws IOException, SQLException, InterruptedException {
        newsImportUtil.getNewsList();
         return "success";
    }

    @GetMapping("/recommend")
    public void recommend() throws IOException, SQLException {
        Long start = System.currentTimeMillis();
        List<Long> userList=new ArrayList<>();
        userList.add(1L);
        userList.add(2L);
        userList.add(3L);
       jobSetter.executeInstantJobForCertainUsers(userList);
//        this.recommendAlgorithm.recommend(userList);
//        jobSetter.executeQuartzJobForCertainUsers(userList);

         Long end = System.currentTimeMillis();
         System.out.println(end-start);
    }



}
