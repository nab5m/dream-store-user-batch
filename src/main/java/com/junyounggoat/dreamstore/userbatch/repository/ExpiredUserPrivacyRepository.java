package com.junyounggoat.dreamstore.userbatch.repository;

import com.junyounggoat.dreamstore.userbatch.dynamodb.ExpiredUserPrivacy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Repository
@RequiredArgsConstructor
public class ExpiredUserPrivacyRepository {
    private static final String EXPIRE_USER_PRIVACY_TABLE_NAME = "expire_user_privacy";

    private final DynamoDbClient dynamoDbClient;

    private final Logger logger = LoggerFactory.getLogger(ExpiredUserPrivacyRepository.class);

    public void putExpiredUserPrivacy(ExpiredUserPrivacy expiredUserPrivacy) {
        ExpiredUserPrivacy withCreationDateTime = expiredUserPrivacy.withNewCreationDateTime();

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(EXPIRE_USER_PRIVACY_TABLE_NAME)
                .item(withCreationDateTime.toMap())
                .build();

        try {
            dynamoDbClient.putItem(putItemRequest);
        } catch (ResourceNotFoundException e) {
            logger.error("DynamoDB table(" + EXPIRE_USER_PRIVACY_TABLE_NAME + ")not found", e);
            throw new RuntimeException(e);
        } catch (DynamoDbException e) {
            logger.error("DynamoDbException", e);
            throw new RuntimeException(e);
        }
    }
}
