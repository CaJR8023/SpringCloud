package com.cajr.springcloud.quartz.runner;

import com.cajr.springcloud.quartz.CBJob;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 17:45
 */
@Component
public class CBCronTriggerRunner {

    public void task(List<Long> users, String cronExpression) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetailImpl jobDetailImpl =
                new JobDetailImpl();
        jobDetailImpl.setJobClass(CBJob.class);
        jobDetailImpl.setKey(new JobKey("CBJob1"));
        jobDetailImpl.getJobDataMap().put("users", users);
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setName("CBCronTrigger1");

        try {
            CronExpression cExp = new CronExpression(cronExpression);
            cronTriggerImpl.setCronExpression(cExp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scheduler.scheduleJob(jobDetailImpl, cronTriggerImpl);

        scheduler.start();
    }
}
