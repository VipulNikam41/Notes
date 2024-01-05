package com.notes.service.auth;

import com.notes.dto.LoginRequest;

import java.util.UUID;

public interface Authorizer {
    String getAccessToken(LoginRequest loginRequest);
    UUID getUserId(String accessToken);
}
