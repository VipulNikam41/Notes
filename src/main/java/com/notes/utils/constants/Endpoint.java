package com.notes.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoint {
    public static final String CREATE_USER = "/api/auth/signup";
    public static final String LOGIN = "/api/auth/login";

    public static final String GET_NOTES_FOR_USER = "/api/notes";
    public static final String GET_NOTE = "/api/notes/{id}";
    public static final String CREATE_NOTE = "/api/notes";
    public static final String UPDATE_NOTE = "/api/notes/{id}";
    public static final String DELETE_NOTE = "/api/notes/{id}";

    public static final String SHARE_NOTE = "/api/notes/{id}/share";
    public static final String SEARCH_NOTE = "/api/search?q=:query";
}
