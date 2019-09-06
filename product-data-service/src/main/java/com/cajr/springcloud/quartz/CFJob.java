package com.cajr.springcloud.quartz;

import com.cajr.springcloud.service.impl.MahoutUserBasedCollaborativeRecommender;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 10:55
 * 每天定时根据用户当日的新闻浏览记录来更新用户的喜好关键词列表
 */
@Component
public class CFJob extends QuartzJobBean {

    @Autowired
    MahoutUserBasedCollaborativeRecommender mahoutUserBasedCollaborativeRecommender;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Long> users = (List<Long>) jobExecutionContext.getJobDetail().getJobDataMap().get("users");
        mahoutUserBasedCollaborativeRecommender.recommend(users);
    }
}
