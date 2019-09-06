package com.cajr.springcloud.quartz.runner;

import com.cajr.springcloud.quartz.HRJob;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 19:27
 */
@Component
public class HRCronTriggerRunner {

    public void task(List<Long> users, String cronExpression) throws SchedulerException
    {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Retrieve a scheduler from schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();

        // Initiate JobDetail with job name, job group, and executable job class
        JobDetailImpl jobDetailImpl =
                new JobDetailImpl();
        jobDetailImpl.setJobClass(HRJob.class);
        jobDetailImpl.setKey(new JobKey("HRJob1"));
        jobDetailImpl.getJobDataMap().put("users",users);
        // Initiate CronTrigger with its name and group name
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setName("HRCronTrigger1");

        try {
            // setup CronExpression
            CronExpression cronExp = new CronExpression(cronExpression);
            // Assign the CronExpression to CronTrigger
            cronTriggerImpl.setCronExpression(cronExp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // schedule a job with JobDetail and Trigger
        scheduler.scheduleJob(jobDetailImpl, cronTriggerImpl);

        // start the scheduler
        scheduler.start();
    }
}
