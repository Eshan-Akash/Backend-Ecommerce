package dev.eshan.productservice.services.interfaces;


import dev.eshan.productservice.dtos.CreateReviewDto;
import dev.eshan.productservice.dtos.ReviewDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.utils.UserData;

import java.util.List;

public interface ReviewService {
    // Method to create a new review for a specific product
    ReviewDto createReview(String productId, CreateReviewDto request, UserData userData) throws NotFoundException;

    // Method to update an existing review (if needed)
    ReviewDto updateReview(String reviewId, CreateReviewDto request, UserData userData);

    // Method to delete a review by its ID
    void deleteReview(String reviewId, UserData userData);

    // Method to fetch all reviews for a specific product
    List<ReviewDto> getReviewsByProduct(String productId);

    // Optional: Method to get a specific review by ID
    ReviewDto getReviewById(String reviewId);
}
