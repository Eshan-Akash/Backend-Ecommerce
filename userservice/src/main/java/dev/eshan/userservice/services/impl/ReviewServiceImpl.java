package dev.eshan.userservice.services.impl;

import dev.eshan.userservice.dtos.CreateReviewRequestDto;
import dev.eshan.userservice.dtos.ReviewDto;
import dev.eshan.userservice.services.interfaces.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    @Override
    public ReviewDto createReview(String productId, CreateReviewRequestDto request) {
        return null;
    }

    @Override
    public List<ReviewDto> getReviewsByProduct(String productId) {
        return null;
    }

    @Override
    public void deleteReview(String reviewId) {

    }

    @Override
    public ReviewDto updateReview(String reviewId, CreateReviewRequestDto request) {
        return null;
    }

    @Override
    public ReviewDto getReviewById(String reviewId) {
        return null;
    }
}
