package com.notes.controller;

import com.notes.dto.AuthTokenResponse;
import com.notes.dto.CreateUser;
import com.notes.dto.LoginRequest;
import com.notes.service.AuthService;
import com.notes.utils.constants.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(Endpoint.CREATE_USER)
    public Boolean createUser(HttpServletRequest request, @RequestBody CreateUser user) {
        return authService.createUser(user);
    }

    @PostMapping(Endpoint.LOGIN)
    public ResponseEntity<AuthTokenResponse> getAuthToken(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        AuthTokenResponse authTokenResponse = authService.getAuthToken(loginRequest);
        return ResponseEntity.ok().body(authTokenResponse);
    }

    @PostMapping(Endpoint.LOGOUT)
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        Boolean logOutUser = authService.logOutUser(request);
        return ResponseEntity.ok().body(logOutUser);
    }
}