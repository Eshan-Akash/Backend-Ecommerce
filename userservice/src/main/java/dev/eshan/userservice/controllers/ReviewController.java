package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.CreateReviewRequestDto;
import dev.eshan.userservice.dtos.ReviewDto;
import dev.eshan.userservice.services.interfaces.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@PathVariable String productId, @Valid @RequestBody CreateReviewRequestDto request) {
        return reviewService.createReview(productId, request);
    }

    @GetMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getReviewsByProduct(@PathVariable String productId) {
        return reviewService.getReviewsByProduct(productId);
    }

    @GetMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getReviewById(@PathVariable String reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @PutMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto updateReview(@PathVariable String reviewId, @Valid @RequestBody CreateReviewRequestDto request) {
        return reviewService.updateReview(reviewId, request);
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
