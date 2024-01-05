package com.notes.dto;

import lombok.Data;

@Data
public class CreateUser {
    private String name;
    private String email;
    private String password;
}
