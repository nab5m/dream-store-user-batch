package com.junyounggoat.dreamstore.userbatch.batch;

import com.junyounggoat.dreamstore.userbatch.service.UserPrivacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DeleteExpiredUserPrivacyJobConfig {
    public static final String JOB_NAME = "DeleteExpiredUserPrivacyJob";
    private final String STEP_NAME = "DeleteExpiredUserPrivacyStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final UserPrivacyService userPrivacyService;

    @Bean
    @Qualifier(JOB_NAME)
    public Job deleteExpiredUserPrivacyJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(deleteExpiredUserPrivacyStep())
                .build();
    }

    @Bean
    public Step deleteExpiredUserPrivacyStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(deleteExpiredUserPrivacyTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Tasklet deleteExpiredUserPrivacyTasklet() {
        return ((contribution, chunkContext) -> {
            userPrivacyService.sendEventDeleteExpiredUserPrivacy();
            return RepeatStatus.FINISHED;
        });
    }
}
