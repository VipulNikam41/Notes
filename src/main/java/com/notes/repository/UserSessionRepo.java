package com.notes.repository;

import com.notes.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserSessionRepo extends JpaRepository<UserSession, UUID> {
    @Query("SELECT us FROM UserSession us " +
            "WHERE us.userId = :userId " +
            "AND us.endsOn > CURRENT_TIMESTAMP")
    List<UserSession> getActiveSessionForUser(UUID userId);

    @Query("SELECT us FROM UserSession us " +
            "INNER JOIN User u ON u.id = us.userId " +
            "WHERE us.id = :userSessionId " +
            "AND u.email = :email")
    UserSession getSessionByIdAndUserEmail(UUID userSessionId, String email);
}
