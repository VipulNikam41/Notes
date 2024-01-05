package com.notes.service.auth;

import com.notes.dto.AuthTokenResponse;
import com.notes.entity.User;
import com.notes.entity.UserSession;
import com.notes.repository.UserRepo;
import com.notes.repository.UserSessionRepo;
import com.notes.utils.EncryptionUtil;
import com.notes.utils.constants.Defaults;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.notes.utils.constants.Defaults.ACCESS_TOKEN_SEPARATOR;

@Component
@RequiredArgsConstructor
public class LocalAuth implements Authorizer {
    private final UserRepo userRepo;
    private final UserSessionRepo sessionRepo;

    @Override
    public AuthTokenResponse createAuthTokenForUser(User user) {
        UserSession newSession = new UserSession();
        newSession.setUserId(user.getId());
        newSession.setEndsOn(
                Date.from(Instant.now().plus(Defaults.AUTH_TOKEN_EXPIRY_TIME, ChronoUnit.MILLIS))
        );

        sessionRepo.save(newSession);

        String authToken = EncryptionUtil.encrypt(user.getEmail() + ACCESS_TOKEN_SEPARATOR + newSession.getId().toString());

        return AuthTokenResponse.builder()
                .authType(Defaults.AUTH_TYPE)
                .authToken(authToken)
                .expiryTime(newSession.getEndsOn().toString())
                .build();
    }

    @Override
    public UUID getUserId(String encryptedAuthToken) {
        List<String> tokenData = this.getSessionParam(encryptedAuthToken);
        String tokenEmail = tokenData.get(0);
        UUID tokenSessionId = UUID.fromString(tokenData.get(1));

        UserSession userSession = sessionRepo.getSessionByIdAndUserEmail(tokenSessionId, tokenEmail);
        if (userSession == null || userSession.isSessionExpired()) {
            return null;
        }

        return userSession.getUserId();
    }

    @Override
    public Boolean expireCurrentSession(String encryptedAuthToken) {
        List<String> tokenData = this.getSessionParam(encryptedAuthToken);
        String tokenEmail = tokenData.get(0);
        UUID tokenSessionId = UUID.fromString(tokenData.get(1));

        UserSession userSession = sessionRepo.getSessionByIdAndUserEmail(tokenSessionId, tokenEmail);
        if (userSession == null) {
            return false;
        }

        if (userSession.isSessionExpired()) {
            return true;
        }

        userSession.setEndsOn(new Date());
        sessionRepo.save(userSession);

        return true;
    }

    private List<String> getSessionParam(String encryptedAuthToken) {
        String authToken = EncryptionUtil.decrypt(encryptedAuthToken);
        if (authToken == null) {
            return Collections.emptyList();
        }

        return List.of(authToken.split(ACCESS_TOKEN_SEPARATOR));
    }
}
