package com.notes.controller;

import com.notes.service.AuthService;
import com.notes.utils.constants.Endpoint;
import com.notes.dto.NoteRequest;
import com.notes.dto.NoteResponse;
import com.notes.dto.ShareNoteRequest;
import com.notes.service.NotesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotesController {
    private final NotesService notesService;

    private final AuthService authService;

    @GetMapping(Endpoint.GET_NOTES_FOR_USER)
    public List<NoteResponse> getNotesForUser(HttpServletRequest request) {
        UUID loggedInUser = authService.getLoggedInUser(request);
        return notesService.getNotesForUser(loggedInUser);
    }

    @GetMapping(Endpoint.GET_NOTE)
    public NoteResponse getNote(HttpServletRequest request, @PathVariable("id") UUID noteId) {
        UUID loggedInUser = authService.getLoggedInUser(request);
        return notesService.getNote(loggedInUser, noteId);
    }

    @PostMapping(Endpoint.CREATE_NOTE)
    public UUID createNote(HttpServletRequest request, @RequestBody NoteRequest noteRequest) {
        UUID loggedInUser = authService.getLoggedInUser(request);
        return notesService.createNote(loggedInUser, noteRequest);
    }

    @PutMapping(Endpoint.UPDATE_NOTE)
    public Boolean updateNote(HttpServletRequest request, @PathVariable("id") UUID noteId, @RequestBody NoteRequest noteRequest) {
        UUID loggedInUser = authService.getLoggedInUser(request);
        return notesService.updateNote(loggedInUser, noteId, noteRequest);
    }

    @DeleteMapping(Endpoint.DELETE_NOTE)
    public Boolean deleteNote(HttpServletRequest request, @PathVariable("id") UUID noteId) {
        UUID loggedInUser = authService.getLoggedInUser(request);
        return notesService.deleteNote(loggedInUser, noteId);
    }

    @PostMapping(Endpoint.SHARE_NOTE)
    public Boolean shareNote(HttpServletRequest request, @PathVariable("id") UUID noteId, @RequestBody ShareNoteRequest shareNoteRequest) {
        UUID loggedInUser = authService.getLoggedInUser(request);
        return notesService.shareNote(loggedInUser, noteId, shareNoteRequest);
    }

    @GetMapping(Endpoint.SEARCH_NOTE)
    public List<NoteResponse> searchNote(HttpServletRequest request, @RequestParam(name = "q") String query) {
        UUID loggedInUser = authService.getLoggedInUser(request);
        return notesService.searchUserNote(loggedInUser, query);
    }
}
