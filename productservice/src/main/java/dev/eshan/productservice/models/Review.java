package dev.eshan.productservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productId", "userId"})
})
public class Review extends BaseModel {
    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Integer rating; // e.g. 1-5 stars

    @Column(nullable = false)
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}