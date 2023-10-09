package com.junyounggoat.dreamstore.userbatch.repository;

import com.junyounggoat.dreamstore.userbatch.constant.QueueName;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
@RequiredArgsConstructor
public class SendingEventRepository {
    private final SqsAsyncClient sqsAsyncClient;


    public void sendEventFindPrivacyExpiredUser() {
        SqsTemplate sqsTemplate = SqsTemplate.builder().sqsAsyncClient(sqsAsyncClient).build();

        sqsTemplate.send(to -> to.queue(QueueName.FIND_PRIVACY_EXPIRED_USER)
                .payload(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
    }
}

