package com.junyounggoat.dreamstore.userbatch.dynamodb;

import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@DynamoDbBean
@Builder(toBuilder = true)
public class ExpiredUserPrivacy {
    private Long userId;
    private User user;
    private UserLoginCredentials userLoginCredentials;
    private LocalDateTime creationDateTime;

    @DynamoDbPartitionKey
    public Long getUserId() {
        return userId;
    }
    public User getUser() {
        return user;
    }
    public UserLoginCredentials getUserLoginCredentials() {
        return userLoginCredentials;
    }
    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    @DynamoDbBean
    @Builder
    public static class User {
        private String userPersonName;

        public String getUserPersonName() {
            return userPersonName;
        }

        public Map<String, AttributeValue> toMap() {
            return Map.of(
                    "user_person_name",
                    AttributeValue.builder().s(userPersonName).build()
            );
        }
    }

    @DynamoDbBean
    @Builder
    public static class UserLoginCredentials {
        private String loginUserName;

        public String getLoginUserName() {
            return loginUserName;
        }

        public Map<String, AttributeValue> toMap() {
            return Map.of(
                    "login_user_name",
                    AttributeValue.builder().s(loginUserName).build()
            );
        }
    }

    public Map<String, AttributeValue> toMap() {
        return Map.of(
                "user_id",
                AttributeValue.builder().n(userId.toString()).build(),
                "user",
                AttributeValue.builder().m(user.toMap()).build(),
                "user_login_credentials",
                AttributeValue.builder().m(userLoginCredentials.toMap()).build(),
                "creation_date_time",
                AttributeValue.builder().s(creationDateTime.format(DateTimeFormatter.ISO_DATE_TIME)).build()
        );
    }

    public ExpiredUserPrivacy withNewCreationDateTime() {
        return this.toBuilder().creationDateTime(LocalDateTime.now()).build();
    }
}
