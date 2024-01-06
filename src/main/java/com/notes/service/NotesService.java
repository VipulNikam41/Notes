package com.notes.service;

import com.notes.dto.NoteRequest;
import com.notes.dto.NoteResponse;
import com.notes.dto.ShareNoteRequest;
import com.notes.entity.Note;
import com.notes.entity.SharedNote;
import com.notes.repository.NotesRepo;
import com.notes.repository.ShareNoteRepo;
import com.notes.repository.UserRepo;
import com.notes.service.mapper.NotesMapper;
import com.notes.utils.BitmapUtil;
import com.notes.utils.constants.NoteAccessBits;
import com.notes.utils.constants.ResponseCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotesService {
    private final NotesRepo notesRepo;
    private final UserRepo userRepo;
    private final ShareNoteRepo shareNoteRepo;

    private final NotesMapper notesMapper;

    public List<NoteResponse> getNotesForUser(UUID loggedInUser) {
        List<Note> notes = notesRepo.findByOwnerId(loggedInUser);

        List<SharedNote> notesSharedWithUser = shareNoteRepo.findByUserId(loggedInUser);

        List<UUID> sharedNoteIds = notesSharedWithUser.stream()
                .filter(sn -> sn.can(NoteAccessBits.READ))
                .map(SharedNote::getNoteId)
                .toList();

        notes.addAll(
                notesRepo.findAllById(sharedNoteIds)
        );

        return notes.stream()
                .map(notesMapper::entityToDto)
                .toList();
    }

    public NoteResponse getNote(UUID loggedInUser, UUID noteId) {
        Note note = notesRepo.findById(noteId).orElse(null);

        if (note != null && !note.getOwnerId().equals(loggedInUser)) {
            SharedNote sharedNote = shareNoteRepo.findByNoteIdAndUserId(noteId, loggedInUser);

            if (sharedNote == null || !sharedNote.can(NoteAccessBits.READ)) {
                return null;
            }
        }

        return notesMapper.entityToDto(note);
    }

    public UUID createNote(UUID loggedInUser, NoteRequest noteRequest) {
        Note note = notesMapper.dtoToEntity(noteRequest);
        note.setOwnerId(loggedInUser);

        notesRepo.save(note);

        return note.getId();
    }

    public Boolean updateNote(UUID loggedInUser, UUID noteId, NoteRequest noteRequest) {
        Note currNote = notesRepo.findById(noteId).orElse(null);
        if (currNote == null) {
            return false;
        }

        if (!currNote.getOwnerId().equals(loggedInUser)) {
            SharedNote sharedNote = shareNoteRepo.findByNoteIdAndUserId(noteId, loggedInUser);
            if (sharedNote == null || !sharedNote.can(NoteAccessBits.EDIT)) {
                log.error(ResponseCode.AUTH_ERROR.getMessage());
                return false;
            }
        }

        Note note = notesMapper.dtoToEntity(noteRequest);
        note.setId(noteId);
        note.setOwnerId(loggedInUser);

        notesRepo.save(note);

        return true;
    }

    @Transactional(rollbackOn = Exception.class)
    public Boolean deleteNote(UUID loggedInUser, UUID noteId) {
        Note note = notesRepo.findById(noteId).orElse(null);
        if (note == null) {
            return false;
        }

        if (!note.getOwnerId().equals(loggedInUser)) {
            SharedNote sharedNote = shareNoteRepo.findByNoteIdAndUserId(noteId, loggedInUser);
            if (sharedNote == null || !sharedNote.can(NoteAccessBits.DELETE)) {
                log.error(ResponseCode.AUTH_ERROR.getMessage());
                return false;
            }
        }

        notesRepo.delete(note);
        shareNoteRepo.deleteByNoteId(note.getId());

        return true;
    }

    public Boolean shareNote(UUID loggedInUser, UUID noteId, ShareNoteRequest shareNoteRequest) {
        Note note = notesRepo.findById(noteId).orElse(null);
        if (note == null) {
            return false;
        }

        if (!note.getOwnerId().equals(loggedInUser)) {
            SharedNote sharedNote = shareNoteRepo.findByNoteIdAndUserId(noteId, loggedInUser);
            if (sharedNote == null || !sharedNote.can(NoteAccessBits.SHARE)) {
                log.error(ResponseCode.AUTH_ERROR.getMessage());
                return false;
            }
        }

        UUID userId = userRepo.getUserIdByEmail(shareNoteRequest.getEmail());

        SharedNote sharedNote = new SharedNote();
        sharedNote.setNoteId(noteId);
        sharedNote.setUserId(userId);
        sharedNote.setAccess(shareNoteRequest.getAccessBits());

        shareNoteRepo.save(sharedNote);

        return true;
    }

    public List<NoteResponse> searchUserNote(UUID loggedInUser, String query) {
        List<Note> notes = notesRepo.findByNoteLike(query, loggedInUser);

        return notes.stream()
                .map(notesMapper::entityToDto)
                .toList();
    }
}
