package com.notes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "notes", indexes = {
        @Index(name = "idx_owner_id", columnList = "ownerId"),
        @Index(name = "idx_title_content", columnList = "title, content")
})
public class Note extends BaseEntity {
    public static final String ENTITY = "Note";

    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String content;
    private String colour;
    private Integer priority;
    private UUID ownerId;
}
