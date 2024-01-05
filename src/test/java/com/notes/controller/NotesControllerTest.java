package com.notes.controller;

import com.notes.dto.NoteRequest;
import com.notes.utils.constants.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

@SpringBootTest
public class NotesControllerTest {
    private static final String BASE_URL = "http://localhost:8080";

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getNotesForUser() {

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

        ResponseEntity<Boolean> response = restTemplate.postForEntity(BASE_URL + Endpoint.CREATE_USER, noteRequest,Boolean.class);

        assertTrue(response.getBody().booleanValue());
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
}