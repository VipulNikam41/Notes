package com.notes.service.auth;

import com.notes.dto.AuthTokenResponse;
import com.notes.entity.User;

import java.util.UUID;

public interface Authorizer {
    AuthTokenResponse createAuthTokenForUser(User user);

    UUID getUserId(String accessToken);

    Boolean expireCurrentSession(String token);
}
