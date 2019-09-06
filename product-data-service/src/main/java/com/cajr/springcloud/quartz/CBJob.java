package com.cajr.springcloud.quartz;

import com.cajr.springcloud.service.impl.ContentBasedRecommender;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 17:34
 */
@Component
public class CBJob extends QuartzJobBean {

    @Autowired
    ContentBasedRecommender contentBasedRecommender;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Long> users=(List<Long>) jobExecutionContext.getJobDetail().getJobDataMap().get("users");
        contentBasedRecommender.recommend(users);
    }
}
