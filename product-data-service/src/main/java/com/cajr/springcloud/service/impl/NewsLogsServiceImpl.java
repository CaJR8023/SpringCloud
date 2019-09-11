package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.mapper.NewslogsMapper;
import com.cajr.springcloud.service.NewsLogsService;
import com.cajr.springcloud.vo.Newslogs;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author CAJR
 * @create 2019/9/5 19:01
 */
@Service
public class NewsLogsServiceImpl implements NewsLogsService {

    @Resource
    NewslogsMapper newslogsMapper;

    @Override
    public List<Newslogs> findAllByUserId(Long userId) {
        return newslogsMapper.findAllByUserId(userId);
    }

    @Override
    public List<Newslogs> findAll() {
        return newslogsMapper.findAll();
    }

    @Override
    public List<Long> hotNewsIdList() {
        List<Newslogs> newsLogsList = findAll();
        Map<Long,Integer> newsLogsMap = new HashMap<>(16);
        List<Long> resultList = new ArrayList<>();
        if (!newsLogsList.isEmpty()){
            Integer nNewsIds = 0;
            for (Newslogs newslogs : newsLogsList) {
                if (!newsLogsMap.containsKey(newslogs.getNewsId())){
                    newsLogsMap.put(newslogs.getNewsId(),nNewsIds);
                }
            }
            for (Newslogs newslogs : newsLogsList) {
                if (newsLogsMap.containsKey(newslogs.getNewsId())){
                    nNewsIds = newsLogsMap.get(newslogs.getNewsId()) +1;
                    newsLogsMap.put(newslogs.getNewsId(),nNewsIds);
                }
            }
        }
        if (!newsLogsMap.isEmpty()){
            List<Map.Entry<Long,Integer>> entryList = new ArrayList<>(newsLogsMap.entrySet());
            entryList.sort(Comparator.comparing(Map.Entry::getValue));
            for (Map.Entry<Long, Integer> e : entryList) {
                resultList.add(e.getKey());
            }
        }

        if (resultList.size() > 6){
            return resultList.subList(0,6);
        }else {
            return resultList;
        }
    }

    @Override
    public Integer add(Newslogs newslogs) {
        return this.newslogsMapper.insertSelective(newslogs);
    }
}
