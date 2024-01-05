package com.notes.service;

import com.notes.dto.AuthTokenResponse;
import com.notes.dto.CreateUser;
import com.notes.dto.LoginRequest;
import com.notes.entity.User;
import com.notes.entity.UserSession;
import com.notes.repository.UserRepo;
import com.notes.repository.UserSessionRepo;
import com.notes.service.auth.Authorizer;
import com.notes.service.mapper.UserMapper;
import com.notes.utils.PasswordUtil;
import com.notes.utils.constants.Defaults;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.notes.utils.constants.Defaults.MAX_SESSION_ALLOWED;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final UserSessionRepo sessionRepo;

    private final UserMapper userMapper;

    private final Authorizer localAuth;


    public Boolean createUser(CreateUser userToCreate) {
        User user = userMapper.dtoToEntity(userToCreate);
        if (userRepo.existsByEmail(user.getEmail())) {
            return false;
        }

        user.setPassword(
                PasswordUtil.hashPassword(userToCreate.getPassword())
        );

        userRepo.save(user);
        return true;
    }

    public AuthTokenResponse getAuthToken(LoginRequest loginRequest) {
        try {
            User user = userRepo.findByEmail(loginRequest.getEmailId());
            if (user == null || !PasswordUtil.matchPassword(loginRequest.getPassword(), user.getPassword())) {
                return null;
            }

            List<UserSession> activeSessions = sessionRepo.getActiveSessionForUser(user.getId());
            if (activeSessions.size() >= MAX_SESSION_ALLOWED) {
                return null;
            }

            return localAuth.createAuthTokenForUser(user);
        } catch (Exception e) {
            return null;
        }
    }

    public UUID getLoggedInUser(HttpServletRequest request) {
        String token = request.getHeader(Defaults.AUTHORIZATION)
                .replace(Defaults.AUTH_TYPE + " ", "");

        return localAuth.getUserId(token);
    }

    public Boolean logOutUser(HttpServletRequest request) {
        String token = request.getHeader(Defaults.AUTHORIZATION)
                .replace(Defaults.AUTH_TYPE + " ", "");

        return localAuth.expireCurrentSession(token);
    }
}
