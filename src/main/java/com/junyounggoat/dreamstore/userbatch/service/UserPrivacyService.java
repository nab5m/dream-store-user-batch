package com.junyounggoat.dreamstore.userbatch.service;

import com.junyounggoat.dreamstore.userbatch.dynamodb.ExpiredUserPrivacy;
import com.junyounggoat.dreamstore.userbatch.repository.ExpiredUserPrivacyRepository;
import com.junyounggoat.dreamstore.userbatch.repository.SendingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPrivacyService {
    private final SendingEventRepository sendingEventRepository;
    private final ExpiredUserPrivacyRepository expiredUserPrivacyRepository;

    public void sendEventFindPrivacyExpiredUser() {
        sendingEventRepository.sendEventFindPrivacyExpiredUser();
    }

    public void backupExpiredUserPrivacy(ExpiredUserPrivacy expiredUserPrivacy) {
        expiredUserPrivacyRepository.putExpiredUserPrivacy(expiredUserPrivacy);
    }
}
