package com.notes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class UserSession extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private Date endsOn;

    public boolean isSessionExpired() {
        return endsOn.toInstant().toEpochMilli() < Instant.now().toEpochMilli();
    }
}
