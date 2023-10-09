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
public class FindPrivacyExpiredUserJobConfig {
    public static final String JOB_NAME = "FindPrivacyExpiredUserJob";
    private final String STEP_NAME = "FindPrivacyExpiredUserStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final UserPrivacyService userPrivacyService;

    @Bean
    @Qualifier(JOB_NAME)
    public Job findPrivacyExpiredUserJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(findPrivacyExpiredUserStep())
                .build();
    }

    @Bean
    public Step findPrivacyExpiredUserStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(findPrivacyExpiredUserTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Tasklet findPrivacyExpiredUserTasklet() {
        return ((contribution, chunkContext) -> {
            userPrivacyService.sendEventFindPrivacyExpiredUser();
            return RepeatStatus.FINISHED;
        });
    }
}
