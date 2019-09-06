package com.cajr.springcloud.quartz;

import com.cajr.springcloud.service.impl.HotRecommender;
import com.cajr.springcloud.service.impl.MahoutUserBasedCollaborativeRecommender;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author CAJR
 * @create 2019/9/5 19:25
 */
@Component
public class HRJob extends QuartzJobBean {
    @Resource
    HotRecommender hotRecommender;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        hotRecommender.getTopHotNewsList().clear();
        hotRecommender.formTodayTopHotNewsList();
    }
}
