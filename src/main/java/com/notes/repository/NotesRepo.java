package com.notes.repository;

import com.notes.entity.Note;
import com.notes.entity.SharedNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotesRepo extends JpaRepository<Note, UUID> {
    List<Note> findByOwnerId(UUID ownerId);

    @Query("SELECT n FROM " + Note.ENTITY + " n " +
            "LEFT JOIN " + SharedNote.ENTITY + " sn ON sn.noteId = n.id " +
            "WHERE n.ownerId = :userId " +
            "OR (sn.userId = :userId AND FUNCTION('BITWISE_AND', sn.access, 2) > 0)")
    List<Note> getAllAccessibleNotesForUser(UUID userId);

    @Query("SELECT n FROM Note n " +
            "LEFT JOIN SharedNote sn ON sn.noteId = n.id " +
            "WHERE (n.ownerId = :userId OR sn.userId = :userId) " +
            "AND (n.content LIKE %:query% OR n.title LIKE %:query%)")
    List<Note> findByNoteLike(String query, UUID userId);
}
