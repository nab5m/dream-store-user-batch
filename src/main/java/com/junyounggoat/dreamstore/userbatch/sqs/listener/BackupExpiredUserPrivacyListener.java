package com.junyounggoat.dreamstore.userbatch.sqs.listener;

import com.junyounggoat.dreamstore.userbatch.batch.DeleteExpiredUserPrivacyJobConfig;
import com.junyounggoat.dreamstore.userbatch.constant.QueueName;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BackupExpiredUserPrivacyListener {
    private final JobLauncher jobLauncher;

    @SqsListener(QueueName.BACKUP_EXPIRED_USER_PRIVACY)
    public void listenBackupExpiredUserPrivacy(@Qualifier(DeleteExpiredUserPrivacyJobConfig.JOB_NAME)
                                               Job deleteExpiredUserPrivacyJob)
    {
        try {
            jobLauncher.run(deleteExpiredUserPrivacyJob, new JobParametersBuilder().toJobParameters());
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
