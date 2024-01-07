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

    @Query(value = "SELECT n.* FROM notes n " +
            "LEFT JOIN shared_note sn ON sn.note_Id = n.id " +
            "WHERE n.owner_Id = :userId " +
            "OR (sn.user_Id = :userId AND (sn.access_bitmap & (1 << :accessLevel)) != 0)", nativeQuery = true)
    List<Note> getAllAccessibleNotesForUser(UUID userId, int accessLevel);

    @Query("SELECT n FROM " + Note.ENTITY + " n " +
            "LEFT JOIN " + SharedNote.ENTITY + " sn ON sn.noteId = n.id " +
            "WHERE (n.ownerId = :userId " +
            "OR (sn.userId = :userId AND MOD(sn.access, 2) != 0)) " +
            "AND (n.content LIKE %:query% OR n.title LIKE %:query%)")
    List<Note> findByNoteLike(String query, UUID userId);
}
