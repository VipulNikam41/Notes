package com.notes.entity;

import com.notes.utils.constants.NoteAccessBits;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@IdClass(SharedNoteId.class)
public class SharedNote extends BaseEntity {
    public static final String ENTITY = "SharedNote";

    @Id
    private UUID noteId;
    @Id
    private UUID userId;

    @Column(name = "access_bitmap")
    private Integer access;

    public boolean can(NoteAccessBits pos) {
        if(access == null) {
            return false;
        }

        return (access & (1 << pos.getBitPosition())) != 0;
    }
}
