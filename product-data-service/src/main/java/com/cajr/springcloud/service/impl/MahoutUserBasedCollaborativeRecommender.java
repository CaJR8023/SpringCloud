package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.service.RecommendAlgorithm;
import com.cajr.springcloud.mapper.NewslogsMapper;
import com.cajr.springcloud.service.NewsLogsService;
import com.cajr.springcloud.service.RecommendationsService;
import com.cajr.springcloud.vo.Newslogs;
import com.cajr.springcloud.vo.Recommendations;
import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author CAJR
 * @create 2019/9/4 15:06
 * TODO Collaborative-Based Filter 基于用户的协同过滤
 */
@Service("MahoutUserBasedCollaborativeRecommender")
public class MahoutUserBasedCollaborativeRecommender implements RecommendAlgorithm {

    public static final Logger logger = Logger.getLogger(MahoutUserBasedCollaborativeRecommender.class);

    @Autowired
    NewslogsMapper newslogsMapper;

    @Autowired
    MySQLBooleanPrefJDBCDataModel model;

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    RecommendService recommendService;

    /**
     * 对应计算相似度时的时效天数
     */
    @Value("${algorithm.CFValidDay}")
    private  int inRecDays;
    /**
     * 给每个用户推荐的新闻的条数
     */
    @Value("${algorithm.CFRecNum}")
    public  int nNews;

    /**
     * 给特定的一批用户进行新闻推荐
     * @param users
     */
    @Override
    public void recommend(List<Long> users) {
        int count=0;
        try {
            System.out.println("CF start at"+new Date());
            MySQLBooleanPrefJDBCDataModel mySQLBooleanPrefJDBCDataModel = this.model;

            List<Newslogs> newslogs = this.newslogsMapper.findAll();


            //移除过期的用户浏览新闻行为，这些行为对计算用户相似度不再具有较大价值
            if (!newslogs.isEmpty()){
                for (Newslogs newsLogs1: newslogs){
                    if (newsLogs1.getViewTime().before(RecommendService.getInRecTimestamp(inRecDays))){
                        mySQLBooleanPrefJDBCDataModel.removePreference(newsLogs1.getUserId(),newsLogs1.getNewsId());
                    }
                }
            }

            UserSimilarity similarity = new LogLikelihoodSimilarity(mySQLBooleanPrefJDBCDataModel);

            // NearestNeighborhood的数量有待考察
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, mySQLBooleanPrefJDBCDataModel);
            Recommender recommender = new GenericUserBasedRecommender(mySQLBooleanPrefJDBCDataModel, neighborhood, similarity);

            for (Long userId : users){
                long start = System.currentTimeMillis();

                List<RecommendedItem> recommendedItems = recommender.recommend(userId,nNews);

                List<Long> hs = new ArrayList<>();
                for (RecommendedItem recommendedItem: recommendedItems) {
                    hs.add(recommendedItem.getItemID());
                }
                if (!hs.isEmpty()){
                    // 过滤掉已推荐新闻和已过期新闻
                    recommendService.filterOutDateNews(hs,userId);
                    recommendService.filterRecCedNews(hs,userId);
                }else {
                    continue;
                }

                if (hs.size()>nNews){
                    recommendService.removeOverNews(hs,nNews);
                }

                recommendService.insertRecommend(userId,hs.iterator(),RecommendAlgorithm.CF);

                count += hs.size();
            }
        } catch (TasteException e) {
            logger.error("CB算法构造偏好对象失败！");
            e.printStackTrace();
        }catch (Exception e)
        {
            logger.error("CB算法数据库操作失败！");
            e.printStackTrace();
        }
        System.out.println("CF has contributed " + (count/users.size()) + " recommending news on average");
        System.out.println("CF finish at "+new Date());
    }

}
