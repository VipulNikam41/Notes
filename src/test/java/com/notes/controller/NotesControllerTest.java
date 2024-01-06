package com.notes.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.notes.dto.*;
import com.notes.utils.constants.Endpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class NotesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    private static String authToken;

    @Test
    public void init() throws Exception {
        CreateUser createUser = new CreateUser();
        createUser.setName("John Doe");
        createUser.setEmail("john.doe@example.com");
        createUser.setPassword("password");

        System.out.println("creating user");
        Boolean responseContent = this.callApi(HttpMethod.POST, Endpoint.CREATE_USER, createUser, Boolean.class);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailId("john.doe@example.com");
        loginRequest.setPassword("password");

        AuthTokenResponse response = this.callApi(HttpMethod.POST,  Endpoint.LOGIN, loginRequest, AuthTokenResponse.class);
        authToken = response.getAuthToken();
    }

    @Test
    public void getNote() {
    }

    @Test
    public void createNote() {
        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setTitle("test note");
        noteRequest.setContent("ertgy tfuyg");
        noteRequest.setPriority(1);
        noteRequest.setColour("#3498db");

        UUID noteId = this.callApi(HttpMethod.POST, Endpoint.CREATE_NOTE, noteRequest, UUID.class);
        System.out.println(noteId);

        List<Map<String, Object>> list = this.callApi(HttpMethod.GET, Endpoint.GET_NOTES_FOR_USER, null, List.class);
        List<NoteResponse> noteResponses = list.stream()
                .map(this::convertToNoteResponse)
                .toList();

        assertEquals(1, noteResponses.size());

        NoteResponse note = this.callApi(HttpMethod.GET, Endpoint.GET_NOTE.replace("{id}", noteId.toString()), null, NoteResponse.class);
        assertEquals(noteResponses.get(0).getId(), note.getId());
    }

    private NoteResponse convertToNoteResponse(Map<String, Object> map) {
        return NoteResponse.builder()
                .id(UUID.fromString((String) map.get("id")))
                .build();
    }

    @Test
    public void updateNote() {
    }

    @Test
    public void deleteNote() {
    }

    @Test
    public void shareNote() {
    }

    @Test
    public void searchNote() {
    }

    private <T> T callApi(HttpMethod method, String url, Object requestBody, Class<T> outputKlass) {
        String userJson;

        userJson = gson.toJson(requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(userJson, headers);


        MvcResult result = null;
        try {
            result = mockMvc.perform(MockMvcRequestBuilders
                            .request(method, url)
                            .content(userJson)
                            .headers(headers)
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