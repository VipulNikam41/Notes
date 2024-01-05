package com.notes.service.auth;

import com.notes.dto.LoginRequest;
import com.notes.entity.User;
import com.notes.entity.UserSession;
import com.notes.repository.UserRepo;
import com.notes.repository.UserSessionRepo;
import com.notes.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.notes.utils.constants.Defaults.ACCESS_TOKEN_SEPARATOR;
import static com.notes.utils.constants.Defaults.MAX_SESSION_ALLOWED;

@Component
@RequiredArgsConstructor
public class LocalAuth implements Authorizer {
    private final UserRepo userRepo;
    private final UserSessionRepo sessionRepo;

    @Override
    public String getAccessToken(LoginRequest loginRequest) {
        User user = userRepo.findUserByEmail(loginRequest.getEmailId());

        if (user == null || !PasswordUtil.matchPassword(loginRequest.getPassword(), user.getPassword())) {
            return null;
        }

        List<UserSession> activeSessions = sessionRepo.findByUserId(user.getId());

        if(activeSessions.size() >= MAX_SESSION_ALLOWED) {
            return null;
        }

        UserSession newSession = new UserSession();
        newSession.setUserId(user.getId());
        newSession.setSession(activeSessions.size());
//        newSession.setEndsOn(new Date().after(new Date(10))); after 10 minutes

        sessionRepo.save(newSession);

        return user.getEmail() + ACCESS_TOKEN_SEPARATOR + newSession.getSession();
    }

    @Override
    public UUID getUserId(String accessToken) {
        List<String> userSession = List.of(accessToken.split(ACCESS_TOKEN_SEPARATOR));

        User user = userRepo.findUserByEmail(userSession.get(0));

        if(user == null) {
            return null;
        }

        UserSession userSessions = sessionRepo.findByUserIdAndSession(user.getId(), Integer.valueOf(userSession.get(1)));

        if(userSessions != null) {
            return user.getId();
        }

        return null;
    }
}
