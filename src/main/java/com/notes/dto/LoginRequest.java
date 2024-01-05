package com.notes.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailId;
    private String password;
}
