package com.cajr.springcloud.quartz.runner;

import com.cajr.springcloud.quartz.CFJob;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 11:28
 */
@Component
public class CFCronTriggerRunner {
    @Autowired
    CFJob cfJob;

    public void task(List<Long> users,String cronExpression) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetailImpl detail = new JobDetailImpl();
        detail.setJobClass(CFJob.class);
        detail.setKey(new JobKey("CFJob1"));
        detail.getJobDataMap().put("users",users);

        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        cronTrigger.setName("CFCronTrigger1");

        try {
            CronExpression cronExpression1 = new CronExpression(cronExpression);
            cronTrigger.setCronExpression(cronExpression1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        scheduler.scheduleJob(detail,cronTrigger);
        scheduler.start();
    }
}
