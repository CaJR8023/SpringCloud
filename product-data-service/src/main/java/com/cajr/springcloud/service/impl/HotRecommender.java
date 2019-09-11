package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.service.NewsLogsService;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.service.RecommendAlgorithm;
import com.cajr.springcloud.service.RecommendationsService;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Newslogs;
import com.cajr.springcloud.vo.Recommendations;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author CAJR
 * @create 2019/9/5 17:54
 */
@Service("HotRecommender")
public  class HotRecommender implements RecommendAlgorithm {
    public static final Logger logger= Logger.getLogger(HotRecommender.class);

    /**
     * 热点新闻的有效时间
     */
    public static int beforeDays = -3;
    /**
     *推荐系统每日为每位用户生成的推荐结果的总数，当CF与CB算法生成的推荐结果数不足此数时，由该算法补充
     */
    public static int TOTAL_REC_NUM = 20;
    /**
     * 将每天生成的“热点新闻”ID，按照新闻的热点程度从高到低放入此List
     */
    private ArrayList<Long> topHotNewsList = new ArrayList<>();

    @Resource
    RecommendationsService recommendationsService;

    @Resource
    NewsLogsService newsLogsService;

    @Resource
    NewsService newsService;

    @Resource
    RecommendService recommendService;

    @Override
    public void recommend(List<Long> users) {
        System.out.println("HR start at "+new Date());
        int count=0;
        Date date = getCertainTimestamp(0, 0, 0);
        for (Long userId : users){
            try {
                //获得已经预备为当前用户推荐的新闻，若数目不足达不到单次的最低推荐数目要求，则用热点新闻补充
                List<Recommendations> recommendations = recommendationsService.findAllByUserId(userId);
                int nNews = 0;
                if (!recommendations.isEmpty()){
                    for (Recommendations r : recommendations) {
                        if (r.getDeriveTime().after(date)){
                            nNews++;
                        }
                    }
                }
                Integer tmpRecNums = 0;
                int delta = TOTAL_REC_NUM;

                if (nNews != 0){
                    tmpRecNums = nNews;
                    delta = TOTAL_REC_NUM - tmpRecNums;
                }
                Set<Long> toBeRecommended = new HashSet<>();
                if (delta > 0){
                    int i = Math.min(topHotNewsList.size(), delta);
                    while (i-- > 0){
                        toBeRecommended.add(topHotNewsList.get(i));
                    }
                    recommendService.filterBrowsedNews(toBeRecommended,userId);
                    recommendService.filterRecCedNews(toBeRecommended,userId);
                    recommendService.insertRecommend(userId, toBeRecommended.iterator(), RecommendAlgorithm.HR);
                    count += toBeRecommended.size();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("HR has contributed " + (users.size()==0?0:count/users.size()) + " recommending news on average");
        System.out.println("HR end at "+new Date());
    }

    private Date getCertainTimestamp(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, Calendar.DECEMBER,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);

        return new Date(calendar.getTime().getTime());
    }

    public  void formTodayTopHotNewsList() {
        topHotNewsList.clear();
        ArrayList<Long> hotNewsTobeRecommended = new ArrayList<>();

        List<Newslogs> newsLogsList = newsLogsService.findAll();
        if (!newsLogsList.isEmpty()){
            for (Newslogs newslogs : newsLogsList){
                if (newslogs.getViewTime().before(recommendService.getSpecificDateFormat(-1))){
                    hotNewsTobeRecommended.add(newslogs.getNewsId());
                }
            }
            List<News> newsList = newsService.findAllTimeDesc();
            for (News n : newsList) {
                hotNewsTobeRecommended.add(n.getId());
            }
            topHotNewsList.addAll(hotNewsTobeRecommended);
        }



    }

    public ArrayList<Long> getTopHotNewsList() {
        return topHotNewsList;
    }

    public void setTopHotNewsList(ArrayList<Long> topHotNewsList) {
        this.topHotNewsList = topHotNewsList;
    }
}