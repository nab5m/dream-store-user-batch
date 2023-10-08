package com.junyounggoat.dreamstore.userbatch.batch;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.junyounggoat.dreamstore.userbatch.dynamodb.ExpiredUserPrivacy;
import com.junyounggoat.dreamstore.userbatch.service.UserPrivacyService;
import lombok.Builder;
import lombok.Getter;
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

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BackupExpiredUserPrivacyJobConfig {
    public static final String JOB_NAME = "BackupExpiredUserPrivacyJob";
    public static final String STEP_NAME = "BackupExpiredUserPrivacyStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ObjectMapper objectMapper;

    private final UserPrivacyService userPrivacyService;

    @Bean
    @Qualifier(JOB_NAME)
    public Job backupExpiredUserPrivacyJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(backupExpiredUserPrivacyStep())
                .build();
    }

    @Bean
    public Step backupExpiredUserPrivacyStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(backupExpiredUserPrivacyTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Tasklet backupExpiredUserPrivacyTasklet() {
        return ((contribution, chunkContext) -> {
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            BackupExpiredUserPrivacyJobParameters backupExpiredUserPrivacyJobParameters =
                    objectMapper.readValue(objectMapper.writeValueAsString(jobParameters), BackupExpiredUserPrivacyJobParameters.class);

            userPrivacyService.backupExpiredUserPrivacy(backupExpiredUserPrivacyJobParameters.getExpiredUserPrivacy());
            return RepeatStatus.FINISHED;
        });
    }

    @Builder
    @Getter
    public static class BackupExpiredUserPrivacyJobParameters {
        private final ExpiredUserPrivacy expiredUserPrivacy;
    }
}
