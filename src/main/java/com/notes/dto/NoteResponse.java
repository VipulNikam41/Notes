package com.notes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NoteResponse {
    private UUID id;
    private String title;
    private String content;
    private String colour;
    private Integer priority;
    private UUID ownerId;
}
