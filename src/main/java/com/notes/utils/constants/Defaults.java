package com.notes.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Defaults {
    public static final Integer MAX_SESSION_ALLOWED = 2;
    public static final String ACCESS_TOKEN_SEPARATOR = ":NOTES:";

    public static final String SECRET_SIGN_KEY = "YourSecretKey";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTH_TYPE = "Bearer";
    public static final int AUTH_TOKEN_EXPIRY_TIME = 600_000;

}
