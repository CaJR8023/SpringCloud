package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.service.RecommendAlgorithm;
import com.cajr.springcloud.common.content.CustomizedHashMap;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.service.UserPrefRefresherService;
import com.cajr.springcloud.util.MapValueComparator;
import com.cajr.springcloud.util.TFIDF;
import com.cajr.springcloud.vo.News;
import org.ansj.app.keyword.Keyword;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.cajr.springcloud.service.impl.UserPrefRefresherServiceImpl.KEY_WORDS_NUM;

/**
 * @Author CAJR
 * @create 2019/9/5 16:42
 * 思路：提取抓取进来的新闻的关键词列表（tf-idf），与每个用户的喜好关键词列表，做关键词相似度计算，取最相似的N个新闻推荐给用户。
 */
@Service("ContentBasedRecommender")
public class ContentBasedRecommender implements RecommendAlgorithm {
    public static final Logger logger = Logger.getLogger(ContentBasedRecommender.class);

    @Value("${algorithm.TFIDFKeywordsNum}")
    private int keyWordsNum;

    @Value("${algorithm.CBRecNum}")
    private int nNews;

    @Autowired
    UserPrefRefresherService userPrefRefresherService;

    @Autowired
    NewsService newsService;

    @Autowired
    RecommendService recommendService;

    @Override
    public void recommend(List<Long> users) {
        try {
            int count = 0;
            System.out.println("CB start at "+ new Date());
            //首先先进行用户喜好的关键字列表的衰减更新+用户当日历史浏览记录的更新
            this.userPrefRefresherService.refresh();
            //新闻及对应关键词的Map
            Map<Long,List<Keyword>> newsKeyWordsMap = new HashMap<>(16);
            Map<Long,Integer> newsModuleMap = new HashMap<>(16);
            //用户喜好关键词列表
            Map<Long, CustomizedHashMap<Integer,CustomizedHashMap<String,Double>>> userPrefListMap = recommendService.getUserPrefListMap(users);

            //获取最近的新闻
            List<News> newsList = this.newsService.findAll();
            if (!newsList.isEmpty()){
                for (News news : newsList) {
                    if (news.getNewsTime().after(recommendService.getInRecDate1())){
                        newsKeyWordsMap.put(news.getId(), TFIDF.getTfidf(news.getTitle(), news.getContent(), KEY_WORDS_NUM));
                        newsModuleMap.put(news.getId(), news.getModuleId());
                    }
                }

                for (Long userId : users){
                    Map<Long,Double> tempMatchMap = new HashMap<>(16);
                    List<Long> newsIdsList = new ArrayList<>(newsKeyWordsMap.keySet());
                    for (Long newsId: newsIdsList) {
                        int moduleId = newsModuleMap.get(newsId);
                        if (userPrefListMap.get(userId).get(moduleId) != null){
                            tempMatchMap.put(newsId,getMatchValue(userPrefListMap.get(userId).get(moduleId), newsKeyWordsMap.get(newsId)));
                        }
                    }
                    //去除匹配值为0的新闻
                    removeZeroItem(tempMatchMap);
                    if (!"{}".equals(tempMatchMap.toString())){
                        tempMatchMap = sortMapByValue(tempMatchMap);
                        Set<Long> toBeReceded = tempMatchMap.keySet();
                        //过滤掉已经推荐过的新闻
                        recommendService.filterRecCedNews(toBeReceded,userId);
                        //过滤掉用户已经看过的新闻
                        recommendService.filterBrowsedNews(toBeReceded,userId);
                        //如果可推荐新闻数目超过了系统默认为CB算法设置的单日推荐上限数（N），则去掉一部分多余的可推荐新闻，剩下的N个新闻才进行推荐
                        if (toBeReceded.size() > nNews){
                            List<Long> toBeRecededList = new ArrayList<>(toBeReceded);
                            recommendService.removeOverNews(toBeRecededList,nNews);
                        }
                        recommendService.insertRecommend(userId,toBeReceded.iterator(),RecommendAlgorithm.CB);
                        count += toBeReceded.size();
                    }
                }
            }
            System.out.println("CB has contributed " + (count/users.size()) + " recommending news on average");
            System.out.println("CB finished at "+new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 使用 Map按value进行排序
     * @param tempMatchMap
     * @return
     */
    private Map<Long, Double> sortMapByValue(Map<Long, Double> tempMatchMap) {
        if (tempMatchMap == null || tempMatchMap.isEmpty()){
            return null;
        }
        Map<Long,Double> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<Long,Double>> entryList = new ArrayList<>(tempMatchMap.entrySet());

        entryList.sort(new MapValueComparator());

        Iterator<Map.Entry<Long, Double>> iter = entryList.iterator();
        Map.Entry<Long, Double> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }

        return sortedMap;
    }

    private void removeZeroItem(Map<Long, Double> tempMatchMap) {
        List<Long> toBeDeleteItem = new ArrayList<>();
        for (Long newsId : tempMatchMap.keySet()){
            if (tempMatchMap.get(newsId) <= 0){
                toBeDeleteItem.add(newsId);
            }
        }

        if (!toBeDeleteItem.isEmpty()){
            for (Long item : toBeDeleteItem) {
                tempMatchMap.remove(item);
            }
        }
    }

    /**
     * 获得用户的关键词列表和新闻关键词列表的匹配程度
     * @param map
     * @param keywords
     * @return
     */
    private Double getMatchValue(CustomizedHashMap<String, Double> map, List<Keyword> keywords) {
        Set<String> keyWordsSet = map.keySet();
        double matchValue = 0;
        for (Keyword keyword : keywords){
            if (keyWordsSet.contains(keyword.getName())){
                matchValue += keyword.getScore()*map.get(keyword.getName());
            }
        }
        return matchValue;
    }
}
