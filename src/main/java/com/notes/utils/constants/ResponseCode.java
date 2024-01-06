package com.notes.utils.constants;

import lombok.Getter;

@Getter
public enum ResponseCode {
    AUTH_ERROR("200", "User unauterized to do following operation"),
    INVALID_SESSION("201", "this session is invalid, please login again");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
