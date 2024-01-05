package com.notes.service.mapper;

import com.notes.dto.NoteRequest;
import com.notes.dto.NoteResponse;
import com.notes.entity.Note;
import org.mapstruct.Mapper;

@Mapper
public interface NotesMapper {
    Note dtoToEntity(NoteRequest request);

    Note dtoToEntity(NoteResponse response);

    NoteResponse entityToDto(Note note);
}
