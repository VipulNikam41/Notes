package com.notes.entity;

import com.notes.utils.BitmapUtil;
import com.notes.utils.constants.NoteAccessBits;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.util.List;
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

    public void setAccess(Integer access) {
        this.access = access;
    }

    public void setAccess(List<NoteAccessBits> noteAccessBits) {
        this.access = BitmapUtil.getBitmapWithBitsSetForPos(noteAccessBits);
    }

    public boolean can(NoteAccessBits pos) {
        return BitmapUtil.isNthBitSet(access, pos.getBitPosition());
    }
}
