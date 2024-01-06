package com.notes.repository;

import com.notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    UUID getUserIdByEmail(String email);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
