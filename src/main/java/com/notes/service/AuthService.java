package com.notes.service;

import com.notes.dto.AuthTokenResponse;
import com.notes.dto.CreateUser;
import com.notes.dto.LoginRequest;
import com.notes.entity.User;
import com.notes.repository.UserRepo;
import com.notes.service.auth.Authorizer;
import com.notes.service.mapper.UserMapper;
import com.notes.utils.PasswordUtil;
import com.notes.utils.constants.Defaults;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final Authorizer localAuth;


    public Boolean createUser(CreateUser userToCreate) {
        if (!PasswordUtil.confirmPassword(userToCreate)) {
            return false;
        }

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
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(), loginRequest.getPassword())
//            );
//
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String token = generateToken(userDetails.getUsername());

            return AuthTokenResponse.builder()
                    .authToken(Defaults.AUTH_TYPE)
                    .authToken(localAuth.getAccessToken(loginRequest))
                    .expiryTime(Defaults.AUTH_TOKEN_EXPIRY_TIME)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    public UUID getLoggedInUser(HttpServletRequest request) {
        String token = request.getHeader(Defaults.AUTHORIZATION)
                .replace(Defaults.AUTH_TYPE + " ", "");

        return localAuth.getUserId(token);
    }

//    private String generateToken(String username) {
//        Date expiryDate = new Date(System.currentTimeMillis() + Defaults.AUTH_TOKEN_EXPIRY_TIME);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, Defaults.SECRET_SIGN_KEY)
//                .compact();
//    }
}
