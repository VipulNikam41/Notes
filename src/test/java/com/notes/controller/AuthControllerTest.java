package com.notes.controller;

import com.google.gson.Gson;
import com.notes.dto.AuthTokenResponse;
import com.notes.dto.CreateUser;
import com.notes.dto.LoginRequest;
import com.notes.entity.User;
import com.notes.repository.UserRepo;
import com.notes.utils.PasswordUtil;
import com.notes.utils.constants.Endpoint;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private UserRepo userRepo;

    @Test
    void createUser() throws Exception {
        CreateUser createUser = new CreateUser();
        createUser.setName("John Doe");
        createUser.setEmail("john.doe@example.com");
        createUser.setPassword("password");

        System.out.println("creating user");
        Boolean responseContent = this.callApi(Endpoint.CREATE_USER, createUser, Boolean.class);

        assertEquals(true, responseContent);
        User savedUser = userRepo.findByEmail(createUser.getEmail());


        assertNotNull(savedUser);
        assertEquals(createUser.getName(), savedUser.getName());
        assertEquals(createUser.getEmail(), savedUser.getEmail());
        assertTrue(PasswordUtil.matchPassword(createUser.getPassword(), savedUser.getPassword()));

        System.out.println(savedUser.getCreatedOn());
    }

    @Test
    void getAuthToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailId("john.doe@example.com");
        loginRequest.setPassword("password");

        AuthTokenResponse responseContent = this.callApi(Endpoint.LOGIN, loginRequest, AuthTokenResponse.class);

        assertNotNull(responseContent.getAuthToken());
    }

    private <T> T callApi(String url, Object requestBody, Class<T> outputKlass) {
        String userJson;

        userJson = gson.toJson(requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(userJson, headers);

        MvcResult result = null;
        try {
            result = mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .content(userJson)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            return gson.fromJson(
                    result.getResponse().getContentAsString(),
                    outputKlass
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}