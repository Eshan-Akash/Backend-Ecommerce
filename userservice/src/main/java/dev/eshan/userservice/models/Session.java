package dev.eshan.userservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Entity
@Data
public class Session extends BaseModel {
    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiringAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;
}