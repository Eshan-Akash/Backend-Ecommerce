package dev.eshan.productservice.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private String reviewId;
    private String productId;
    private String userId;
    private String username;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
