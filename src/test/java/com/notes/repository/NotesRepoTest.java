package com.notes.repository;

import com.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import static org.junit.Assert.*;

public interface NotesRepoTest extends JpaRepository<Note, UUID> {

}