package com.notes.controller;

import com.google.gson.Gson;
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
    public void createNote() {
        authToken = createAndLogInUserToGetAuthToken();

        UUID noteId1 = this.createANotes(1);
        NoteResponse note = this.getNote(noteId1);
        assertNotNull(note);

        UUID noteId2 = this.createANotes(2);
        UUID noteId3 = this.createANotes(3);
        UUID noteId4 = this.createANotes(4);

        List<NoteResponse> noteResponses = this.getAllNotes();
        assertEquals(4, noteResponses.size());

        this.updateNote(noteId2);
        note = this.getNote(noteId2);
        assertEquals(ObjectStore.updatedNoteObject().getTitle(), note.getTitle());

        this.deleteNote(noteId3);
        note = this.getNote(noteId3);
        assertNull(note);

        List<NoteResponse> results = this.searchNote(" 4");
        note = results.stream().filter(n -> n.getId().equals(noteId4)).findFirst().orElse(null);
        assertNotNull(note);
    }

    private String createAndLogInUserToGetAuthToken() {
        CreateUser createUser = new CreateUser();
        createUser.setName("John Doe");
        createUser.setEmail("john.doe@example.com");
        createUser.setPassword("password");

        System.out.println("creating user");
        Boolean result = this.callApi(HttpMethod.POST, Endpoint.CREATE_USER, createUser, Boolean.class);
        assertTrue(result);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailId("john.doe@example.com");
        loginRequest.setPassword("password");

        AuthTokenResponse response = this.callApi(HttpMethod.POST, Endpoint.LOGIN, loginRequest, AuthTokenResponse.class);
        assertNotNull(response);
        assertNotNull(response.getAuthToken());

        return response.getAuthToken();
    }

    private UUID createANotes(int noteNum) {
        NoteRequest noteRequest = ObjectStore.newNoteObject(noteNum);
        return this.callApi(HttpMethod.POST, Endpoint.CREATE_NOTE, noteRequest, UUID.class);
    }

    private NoteResponse getNote(UUID noteId) {
        return this.callApi(
                HttpMethod.GET,
                Endpoint.GET_NOTE.replace("{id}", noteId.toString()),
                null,
                NoteResponse.class
        );
    }

    private List<NoteResponse> getAllNotes() {
        List<Map<String, Object>> list = this.callApi(HttpMethod.GET, Endpoint.GET_NOTES_FOR_USER, null, List.class);
        return list.stream()
                .map(this::convertToNoteResponse)
                .toList();
    }

    private NoteResponse convertToNoteResponse(Map<String, Object> map) {
        return NoteResponse.builder()
                .id(UUID.fromString((String) map.get("id")))
                .title((String) map.get("title"))
                .content((String) map.get("content"))
                .colour((String) map.get("colour"))
                .priority(((Double) map.get("priority")).intValue())
                .ownerId(UUID.fromString((String) map.get("ownerId")))
                .build();
    }

    private void updateNote(UUID noteId) {
        NoteRequest noteRequest = ObjectStore.updatedNoteObject();
        Boolean updated = this.callApi(HttpMethod.PUT, Endpoint.UPDATE_NOTE.replace("{id}", noteId.toString()), noteRequest, Boolean.class);
        assertTrue(updated);
    }

    private void deleteNote(UUID noteId) {
        Boolean deleted = this.callApi(HttpMethod.DELETE, Endpoint.DELETE_NOTE.replace("{id}", noteId.toString()), null, Boolean.class);
        assertTrue(deleted);
    }

    private List<NoteResponse> searchNote(String query) {
        List<Map<String, Object>> list = this.callApi(HttpMethod.GET, Endpoint.SEARCH_NOTE + "?q="+query, null, List.class);
        return list.stream()
                .map(this::convertToNoteResponse)
                .toList();
    }

    private void shareNote(UUID noteId) {
        ShareNoteRequest shareNoteRequest = ObjectStore.readOnlyShare("email@gm.com");
        Boolean updated = this.callApi(HttpMethod.GET, Endpoint.SHARE_NOTE.replace("{id}", noteId.toString()), shareNoteRequest, Boolean.class);
        assertTrue(updated);
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

            return gson.fromJson(
                    result.getResponse().getContentAsString(),
                    outputKlass
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}