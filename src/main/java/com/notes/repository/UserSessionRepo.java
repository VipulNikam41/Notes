package com.notes.repository;

import com.notes.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserSessionRepo extends JpaRepository<UserSession, UUID> {
    List<UserSession> findByUserId(UUID id);

    UserSession findByUserIdAndSession(UUID userId, Integer session);
}
