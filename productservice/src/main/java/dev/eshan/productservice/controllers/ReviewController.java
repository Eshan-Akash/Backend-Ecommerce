package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.CreateReviewDto;
import dev.eshan.productservice.dtos.ReviewDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.ReviewService;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@PathVariable String productId, @Valid @RequestBody CreateReviewDto reviewDto)
            throws NotFoundException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return reviewService.createReview(productId, reviewDto, userData);
    }

    @PutMapping("/{reviewId}")
    public ReviewDto updateReview(@PathVariable String reviewId, @Valid @RequestBody CreateReviewDto reviewDto) throws NotFoundException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return reviewService.updateReview(reviewId, reviewDto, userData);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable String reviewId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        reviewService.deleteReview(reviewId, userData);
    }

    @GetMapping("/product/{productId}")
    public List<ReviewDto> getReviewsByProduct(@PathVariable String productId) {
        return reviewService.getReviewsByProduct(productId);
    }

    @GetMapping("/{reviewId}")
    public ReviewDto getReviewById(@PathVariable String reviewId) {
        return reviewService.getReviewById(reviewId);
    }
}
