package dev.eshan.userservice.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long reviewId;
    private Long productId;
    private Long userId;
    private String username;  // The name of the user who left the review
    private int rating;       // Rating, for example from 1 to 5
    private String reviewText;
    private LocalDateTime reviewDate;
}
