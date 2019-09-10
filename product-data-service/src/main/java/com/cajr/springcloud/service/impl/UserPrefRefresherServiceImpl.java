package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.mapper.NewsMapper;
import com.cajr.springcloud.mapper.NewslogsMapper;
import com.cajr.springcloud.mapper.UsersMapper;
import com.cajr.springcloud.common.content.CustomizedHashMap;
import com.cajr.springcloud.service.UserPrefRefresherService;
import com.cajr.springcloud.util.JsonUtil;
import com.cajr.springcloud.util.TFIDF;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Newslogs;
import com.cajr.springcloud.vo.Users;
import org.ansj.app.keyword.Keyword;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author CAJR
 * @create 2019/9/5 13:47
 */
@Service("UserPrefRefresherServiceImpl")
public class UserPrefRefresherServiceImpl implements UserPrefRefresherService {

    //设置TFIDF提取的关键字数目
    public static final int KEY_WORDS_NUM = 10;

    //每日衰减系数coefficient
    private static final double DEC_COEFFICIENT=0.7;

    @Resource
    UsersMapper usersMapper;
    @Resource
    NewslogsMapper newslogsMapper;
    @Resource
    NewsMapper newsMapper;
    @Resource
    RecommendService recommendService;

    @Override
    public void refresh() {
        refresh(recommendService.getUserList());
    }

    /**
     * 按照推荐频率调用的方法，一般为一天执行一次。
     * 定期根据前一天所有用户的浏览记录，在对用户进行喜好关键词列表TFIDF值衰减的后，将用户前一天看的新闻的关键词及相应TFIDF值更新到列表中去。
     * @param userIdsList
     */
    private void refresh(List<Long> userIdsList) {
        //首先对用户的喜好关键词列表进行衰减更新
        autoDecRefresh();
        //用户浏览新闻纪录：userBrowsedMap:<Long(userId),ArrayList<String>(newsId List)>
        Map<Long,List<Long>> userBrowsedMap = getBrowsedHistoryMap();
        assert userBrowsedMap != null;
        if (!userBrowsedMap.isEmpty()){
            //用户喜好关键词列表：userPrefListMap:<String(userId),String(json))>
            Map<Long,CustomizedHashMap<Integer,CustomizedHashMap<String,Double>>> userPrefListMap = recommendService.getUserPrefListMap(userBrowsedMap.keySet());
            //新闻对应关键词列表与模块ID：newsTFIDFMap:<String(newsId),List<Keyword>>,<String(newsModuleId),Integer(moduleId)>
            Map<String,Object> newsTFIDFMap = getNewsTFIDFMap();

            //开始遍历用户浏览记录，更新用户喜好关键词列表
            //对每个用户（外层循环），循环他所看过的每条新闻（内层循环），对每个新闻，更新它的关键词列表到用户的对应模块中
            Set<Long> userIdSet = userBrowsedMap.keySet();
            if (!userIdSet.isEmpty()){
                for (Long userId : userIdSet){
                    List<Long> newsIdList = userBrowsedMap.get(userId);
                    if (!newsIdList.isEmpty()){
                        for (Long newsId : newsIdList) {
                            Integer moduleId = (Integer) newsTFIDFMap.get(newsId+"moduleId");
                            //获得对应模块的map
                            CustomizedHashMap<String,Double> rateMap = userPrefListMap.get(userId).get(moduleId);
                            System.out.println(rateMap.toString());
                            //获取新闻的(关键字:TFIDF)的List
                            List<Keyword> keywordList = (List<Keyword>) newsTFIDFMap.get(newsId.toString());
                            if (!keywordList.isEmpty()){
                                for (Keyword keyword :
                                        keywordList) {
                                    String name = keyword.getName();
                                    System.out.println(name);
                                    if (rateMap.containsKey(name)) {
                                        rateMap.put(name, rateMap.get(name) + keyword.getScore());
                                    } else {
                                        rateMap.put(name, keyword.getScore());
                                    }
                                }
                                userPrefListMap.get(userId);
                            }
                        }
                    }
                }

            }
            Set<Long> userIds =userBrowsedMap.keySet();
            for (Long userId : userIds) {
                this.usersMapper.updatePrefListByPrimaryKey(userId,userPrefListMap.get(userId).toString());
            }

        }
    }

    /**
     *  将所有当天被浏览过的新闻提取出来，以便进行TFIDF求值操作，以及对用户喜好关键词列表的更新。
     * @return
     */
    private Map<String, Object> getNewsTFIDFMap() {
        Map<String, Object> newsTFIDFMap = new HashMap<>(16);
        HashSet<Long> newsSet = getBrowsedNewsSet();
        List<Long> newsIdList = new ArrayList<>(newsSet);
        List<News> newsList = this.newsMapper.findSectionNewsById(newsIdList);
        if (!newsList.isEmpty()){
            newsList.forEach(news -> {
                newsTFIDFMap.put(String.valueOf(news.getId()), TFIDF.getTfidf(news.getTitle(),news.getContent(),KEY_WORDS_NUM));
                newsTFIDFMap.put(news.getId()+"moduleId",news.getModuleId());
            });
        }

        return newsTFIDFMap;
    }

    /**
     * 获得浏览过的新闻的集合
     * @return
     */
    private HashSet<Long> getBrowsedNewsSet() {
        Map<Long,List<Long>> browsedMap = getBrowsedHistoryMap();
        HashSet<Long> newsIdSet = new HashSet<>();
        Iterator<Long> ite=getBrowsedHistoryMap().keySet().iterator();
        while(ite.hasNext()){
            Iterator<Long> inIte=browsedMap.get(ite.next()).iterator();
            while(inIte.hasNext()){
                newsIdSet.add(inIte.next());
            }
        }
        return newsIdSet;
    }

    /**
     *  提取出当天有浏览行为的用户及其各自所浏览过的新闻id列表
     * @return
     */
    private Map<Long, List<Long>> getBrowsedHistoryMap() {
        Map<Long, List<Long>> userBrowsedMap = new HashMap<>(16);
        List<Newslogs> newsLogsList = this.newslogsMapper.findAll();
        for (Newslogs n : newsLogsList) {
            if (n.getViewTime().after(recommendService.getSpecificDateFormat(-1))){
                userBrowsedMap.put(n.getUserId(),new ArrayList<>());
                userBrowsedMap.get(n.getUserId()).add(n.getNewsId());
            }
        }

        return userBrowsedMap;
    }

    @Override
    public void autoDecRefresh() {
        autoDecRefresh(recommendService.getUserList());
    }

    private void autoDecRefresh(List<Long> userIdList) {
        List<Users> userList = this.usersMapper.findSectionUsers(userIdList);
        //用于删除喜好值过低的关键词
        List<String> keyWordToDelete = new ArrayList<>();
        if (!userList.isEmpty()){
            userList.forEach((user)->{
                StringBuilder newPrefList = new StringBuilder("{");
                HashMap<Integer, CustomizedHashMap<String,Double>> map = JsonUtil.jsonPrefListtoMap(user.getPrefList());
                Iterator<Integer> integerIterator = map.keySet().iterator();
                while (integerIterator.hasNext()){
                    //用户对应模块的喜好不为空
                    Integer moduleId = integerIterator.next();
                    CustomizedHashMap<String,Double> moduleMap = map.get(moduleId);
                    newPrefList.append("\"").append(moduleId).append("\":");
                    if (!("{}".equals(moduleMap.toString()))){
                        Iterator<String> stringIterator = moduleMap.keySet().iterator();
                        while(stringIterator.hasNext()){
                            String key = stringIterator.next();
                            //累计TFIDF值*衰减系数
                            double result = moduleMap.get(key)*DEC_COEFFICIENT;
                            if (result<10){
                                keyWordToDelete.add(key);
                            }
                            moduleMap.put(key,result);
                        }
                    }
                    for (String deleteKey: keyWordToDelete) {
                        moduleMap.remove(deleteKey);
                    }
                    keyWordToDelete.clear();
                    newPrefList.append(moduleMap.toString()).append(",");
                }
//                newPrefList = "'"+newPrefList.substring(0,newPrefList.length()-1)+"}'";
                newPrefList.append(",").append(newPrefList.substring(0,newPrefList.length()-1)).append("}");
                String s = "{";
            });
        }
    }
}
