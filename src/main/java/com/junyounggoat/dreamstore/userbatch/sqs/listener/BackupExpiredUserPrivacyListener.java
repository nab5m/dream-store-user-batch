package com.junyounggoat.dreamstore.userbatch.sqs.listener;

import com.junyounggoat.dreamstore.userbatch.batch.BackupExpiredUserPrivacyJobConfig;
import com.junyounggoat.dreamstore.userbatch.constant.QueueName;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class BackupExpiredUserPrivacyListener {
    private final JobLauncher jobLauncher;
    private final Job backupExpiredUserPrivacyJob;

    public BackupExpiredUserPrivacyListener(JobLauncher jobLauncher,
                                            @Qualifier(BackupExpiredUserPrivacyJobConfig.JOB_NAME)
                                            Job backupExpiredUserPrivacyJob) {
        this.jobLauncher = jobLauncher;
        this.backupExpiredUserPrivacyJob = backupExpiredUserPrivacyJob;
    }

    @SqsListener(QueueName.BACKUP_EXPIRED_USER_PRIVACY)
    public void listenBackupExpiredUserPrivacy(@Payload String messageBody)
    {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(BackupExpiredUserPrivacyJobConfig.JOB_PARAMETER_MESSAGE_BODY_KEY, messageBody)
                .toJobParameters();
        try {
            jobLauncher.run(backupExpiredUserPrivacyJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
