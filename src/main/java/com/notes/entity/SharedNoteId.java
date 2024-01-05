package com.notes.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class SharedNoteId {
    private UUID noteId;
    private UUID userId;
}
