package com.cajr.springcloud.main;

import com.cajr.springcloud.quartz.runner.CBCronTriggerRunner;
import com.cajr.springcloud.quartz.runner.CFCronTriggerRunner;
import com.cajr.springcloud.quartz.runner.HRCronTriggerRunner;
import com.cajr.springcloud.service.impl.ContentBasedRecommender;
import com.cajr.springcloud.service.impl.HotRecommender;
import com.cajr.springcloud.service.impl.MahoutUserBasedCollaborativeRecommender;
import com.cajr.springcloud.service.impl.RecommendService;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 20:36
 */
@Component
public class JobSetter {
    public static final Logger logger= Logger.getLogger(JobSetter.class);

    @Value("${algorithm.startAt}")
    private String cronExpression;

    @Value("${algorithm.enableCF}")
    private boolean enableCF;

    @Value("${algorithm.enableCB}")
    private boolean enableCB;

    @Value("${algorithm.enableHR}")
    private boolean enableHR;

    @Autowired
    HotRecommender hotRecommender;

    @Autowired
    MahoutUserBasedCollaborativeRecommender mahoutUserBasedCollaborativeRecommender;

    @Autowired
    ContentBasedRecommender contentBasedRecommender;

    @Autowired
    CFCronTriggerRunner cfCronTriggerRunner;
    @Autowired
    CBCronTriggerRunner cbCronTriggerRunner;
    @Autowired
    HRCronTriggerRunner hrCronTriggerRunner;


    @Resource
    RecommendService recommendService;
    /**
     * 使用Quartz的表达式进行时间设定（默认为每天0点开始工作），详情请参照：http://www.quartz-scheduler.org/api/2.2.1/index.html(CronExpression)
     * 当启用该方法时，推荐系统可以保持运行，直到被强制关闭。
     * @param userList
     */
    private void executeQuartzJob(List<Long> userList) {
        try
        {
            if(enableCF){
                cfCronTriggerRunner.task(userList,cronExpression);
            }
            if(enableCB){
                cbCronTriggerRunner.task(userList,cronExpression);
            }
            if(enableHR){
                hrCronTriggerRunner.task(userList,cronExpression);
            }
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
        System.out.println("本次推荐结束于"+new Date());
    }


    /**
     * 为指定用户执行定时新闻推荐
     * @param goalUserList 目标用户的id列表
     */
    public void executeQuartzJobForCertainUsers(List<Long> goalUserList) {
        executeQuartzJob(goalUserList);
    }

    /**
     * 为所有用户执行定时新闻推荐
     */
    public void executeQuartzJobForAllUsers() {
        executeQuartzJob(recommendService.getUserList());
    }

    /**
     * 为活跃用户进行定时新闻推荐。
     *
     */
    public void executeQuartzJobForActiveUsers() {
        executeQuartzJob(recommendService.getActiveUsers());
    }


    /**
     * 执行一次新闻推荐
     * 参数forActiveUsers表示是否只针对活跃用户进行新闻推荐，true为是，false为否。
     */
    private void executeInstantJob(List<Long> userIDList) {
        //让热点新闻推荐器预先生成今日的热点新闻
        hotRecommender.formTodayTopHotNewsList();

        if(enableCF){
            mahoutUserBasedCollaborativeRecommender.recommend(userIDList);
        }
        if(enableCB){
            contentBasedRecommender.recommend(userIDList);
        }
        if(enableHR){
            hotRecommender.recommend(userIDList);
        }

        System.out.println("本次推荐结束于"+new Date());
    }

    /**
     * 执行一次新闻推荐
     * 参数forActiveUsers表示是否只针对活跃用户进行新闻推荐，true为是，false为否。
     */
    public void executeInstantJobForCertainUsers(List<Long> goalUserList) {
        executeInstantJob(goalUserList);
    }

    /**
     * 为所用有用户执行一次新闻推荐
     */
    public void executeInstantJobForAllUsers() {
        executeInstantJob(recommendService.getUserList());
    }

    /**
     * 为活跃用户进行一次推荐。
     */
    public void executeInstantJobForActiveUsers() {
        executeInstantJob(recommendService.getActiveUsers());
    }
}
