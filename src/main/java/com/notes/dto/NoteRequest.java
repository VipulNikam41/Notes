package com.notes.dto;

import lombok.Data;

@Data
public class NoteRequest {
    private String title;
    private String content;
    private String colour;
    private Integer priority;
}
