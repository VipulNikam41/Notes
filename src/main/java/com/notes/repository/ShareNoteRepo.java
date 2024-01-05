package com.notes.repository;

import com.notes.entity.SharedNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShareNoteRepo extends JpaRepository<SharedNote, UUID> {
    SharedNote findByNoteIdAndUserId(UUID noteId, UUID userId);

    List<SharedNote> findByUserId(UUID loggedInUser);

    void deleteByNoteId(UUID noteId);
}
