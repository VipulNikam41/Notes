package com.notes.dto;

import com.notes.utils.constants.NoteAccessBits;
import lombok.Data;

import java.util.List;

@Data
public class ShareNoteRequest {
    String email;
    List<NoteAccessBits> accessBits;
}
