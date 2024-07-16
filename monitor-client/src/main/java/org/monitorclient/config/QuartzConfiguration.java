package org.monitorclient.config;

import lombok.extern.slf4j.Slf4j;
import org.monitorclient.task.MonitorJobBean;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: monitor
 * @description: spring-quartz配置类
 * @author: 王贝强
 * @create: 2024-07-15 18:26
 */
@Slf4j
@Configuration
public class QuartzConfiguration {
    @Bean
    public JobDetail jobDetailFactoryBean(){
        return JobBuilder.newJob(MonitorJobBean.class)
                .withIdentity("monitor-task")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cronTriggerFactoryBean(JobDetail detail){
        CronScheduleBuilder cron=CronScheduleBuilder.cronSchedule("*/10 * * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(detail)
                .withIdentity("monitor-trigger")
                .withSchedule(cron)
                .build();
    }
}
