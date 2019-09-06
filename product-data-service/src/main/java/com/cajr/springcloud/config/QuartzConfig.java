package com.cajr.springcloud.config;

import com.cajr.springcloud.quartz.CFJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * @Author CAJR
 * @create 2019/9/5 11:14
 */
@Configuration
public class QuartzConfig {

//    @Bean(name = "CFJob")
//    public MethodInvokingJobDetailFactoryBean jobDetailToCFJob(CFJob cfJob){
//        MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
//
//        jobDetailFactory.setConcurrent(false);
//        jobDetailFactory.setTargetObject(cfJob);
//        jobDetailFactory.setTargetMethod("executeInternal");
//
//        return jobDetailFactory;
//    }
}
