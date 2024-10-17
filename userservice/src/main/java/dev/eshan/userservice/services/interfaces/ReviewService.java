package dev.eshan.userservice.services.interfaces;

import dev.eshan.userservice.dtos.CreateReviewRequestDto;
import dev.eshan.userservice.dtos.ReviewDto;

import java.util.List;

public interface ReviewService {
    // Method to create a new review for a specific product
    ReviewDto createReview(String productId, CreateReviewRequestDto request);

    // Method to fetch all reviews for a specific product
    List<ReviewDto> getReviewsByProduct(String productId);

    // Method to delete a review by its ID
    void deleteReview(String reviewId);

    // Method to update an existing review (if needed)
    ReviewDto updateReview(String reviewId, CreateReviewRequestDto request);

    // Optional: Method to get a specific review by ID
    ReviewDto getReviewById(String reviewId);
}
