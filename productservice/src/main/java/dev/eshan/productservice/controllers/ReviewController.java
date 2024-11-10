package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.CreateReviewDto;
import dev.eshan.productservice.dtos.ReviewDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.ReviewService;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@PathVariable String productId, @Valid @RequestBody CreateReviewDto reviewDto)
            throws NotFoundException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return reviewService.createReview(productId, reviewDto, userData);
        } catch (Exception e) {
            log.error("Error occurred while creating review for productId: {}", productId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while creating review for productId: " + productId);
        }
    }

    @PutMapping("/{reviewId}")
    public ReviewDto updateReview(@PathVariable String reviewId, @Valid @RequestBody CreateReviewDto reviewDto)
            throws NotFoundException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return reviewService.updateReview(reviewId, reviewDto, userData);
        } catch (Exception e) {
            log.error("Error occurred while updating review with id: {}", reviewId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while updating review with id: " + reviewId);
        }
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable String reviewId) {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            reviewService.deleteReview(reviewId, userData);
        } catch (Exception e) {
            log.error("Error occurred while deleting review with id: {}", reviewId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while deleting review with id: " + reviewId);
        }
    }

    @GetMapping("/product/{productId}")
    public List<ReviewDto> getReviewsByProduct(@PathVariable String productId) {
        try {
            return reviewService.getReviewsByProduct(productId);
        } catch (Exception e) {
            log.error("Error occurred while fetching reviews for productId: {}", productId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while fetching reviews for productId: " + productId);
        }
    }

    @GetMapping("/{reviewId}")
    public ReviewDto getReviewById(@PathVariable String reviewId) {
        try {
            return reviewService.getReviewById(reviewId);
        } catch (Exception e) {
            log.error("Error occurred while fetching review with id: {}", reviewId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while fetching review with id: " + reviewId);
        }
    }
}
