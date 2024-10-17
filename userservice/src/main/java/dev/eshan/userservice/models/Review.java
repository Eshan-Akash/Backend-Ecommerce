package dev.eshan.userservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews")
public class Review extends BaseModel {
    @Column(nullable = false)
    private String reviewText;

    @Column(nullable = false)
    private Integer rating; // e.g. 1-5 stars

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String productId; // Assuming productId is stored separately

    private LocalDateTime createdAt = LocalDateTime.now();
}