package com.notes.controller;

import com.notes.dto.NoteRequest;
import com.notes.dto.NoteResponse;
import com.notes.dto.ShareNoteRequest;
import com.notes.utils.constants.NoteAccessBits;

import java.util.List;

public class ObjectStore {
    private static final NoteRequest updateNoteObject = new NoteRequest();

    public static NoteRequest newNoteObject(int noteNum) {
        NoteRequest newNoteObject = new NoteRequest();
        newNoteObject.setTitle("Test note: " + noteNum);
        newNoteObject.setContent(noteNum + " note is being added");
        newNoteObject.setPriority(noteNum % 5);
        newNoteObject.setColour("#3498db");

        return newNoteObject;
    }

    public static NoteRequest updatedNoteObject() {
        updateNoteObject.setTitle("Updated Note");
        updateNoteObject.setContent("note updated");
        updateNoteObject.setPriority(2);
        updateNoteObject.setColour("#3498db");

        return updateNoteObject;
    }

    public static ShareNoteRequest readOnlyShare(String email) {
        ShareNoteRequest shareNoteRequest = new ShareNoteRequest();
        shareNoteRequest.setEmail(email);
        shareNoteRequest.setAccessBits(List.of(NoteAccessBits.READ));
        return shareNoteRequest;

    }
}
