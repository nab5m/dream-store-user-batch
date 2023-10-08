package com.junyounggoat.dreamstore.userbatch.scheduler;


import com.junyounggoat.dreamstore.userbatch.batch.DeleteExpiredUserPrivacyJobConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExpireUserPrivacyJob implements Job {
    private final JobLauncher batchJobLauncher;

    private final org.springframework.batch.core.Job deleteExpiredUserPrivacyJob;

    public ExpireUserPrivacyJob(JobLauncher batchJobLauncher,
                                @Qualifier(DeleteExpiredUserPrivacyJobConfig.JOB_NAME)
                                org.springframework.batch.core.Job deleteExpiredUserPrivacyJob)
    {
        this.batchJobLauncher = batchJobLauncher;
        this.deleteExpiredUserPrivacyJob = deleteExpiredUserPrivacyJob;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            batchJobLauncher.run(deleteExpiredUserPrivacyJob, new JobParametersBuilder().toJobParameters());
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
