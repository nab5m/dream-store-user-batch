package com.junyounggoat.dreamstore.userbatch.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpireUserPrivacyJobConfig {
    public static final String JOB_DETAIL_NAME = "expireUserPrivacyJobDetail";
    public static final String TRIGGER_NAME = "expireUserPrivacyJobTrigger";

    @Bean
    @Qualifier(JOB_DETAIL_NAME)
    public JobDetail expireUserPrivacyJobDetail() {
        return JobBuilder.newJob()
                .ofType(ExpireUserPrivacyJob.class)
                .withIdentity(ExpireUserPrivacyJob.class.getSimpleName())
                .withDescription("사용자 개인정보 만료 처리")
                .storeDurably(true)
                .build();
    }

    @Bean
    @Qualifier(TRIGGER_NAME)
    public Trigger expireUserPrivacyJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(expireUserPrivacyJobDetail())
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(3, 0))
                .build();
    }
}
