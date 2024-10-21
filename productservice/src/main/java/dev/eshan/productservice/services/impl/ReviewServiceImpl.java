package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.CreateReviewDto;
import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.dtos.ReviewDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.models.Review;
import dev.eshan.productservice.repositories.ReviewRepository;
import dev.eshan.productservice.services.interfaces.ProductService;
import dev.eshan.productservice.services.interfaces.ReviewService;
import dev.eshan.productservice.utils.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             @Qualifier("productServiceImpl") ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
    }

    @Override
    public ReviewDto createReview(String productId, CreateReviewDto request, UserData userData) throws NotFoundException {
        GenericProductDto productDto = productService.getProductById(productId);
        if (productDto == null || productDto.getId() == null) {
            throw new NotFoundException("Product not found by id: " + productId);
        }

        Review review = new Review();
        review.setProductId(productId);
        review.setUserId(userData.getUserId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(savedReview.getId());
        reviewDto.setProductId(savedReview.getProductId());
        reviewDto.setUserId(savedReview.getUserId());
        reviewDto.setRating(savedReview.getRating());
        reviewDto.setComment(savedReview.getComment());
        reviewDto.setCreatedAt(savedReview.getCreatedAt());
        reviewDto.setUpdatedAt(savedReview.getUpdatedAt());

        return reviewDto;
    }

    @Override
    public ReviewDto updateReview(String reviewId, CreateReviewDto request, UserData userData) throws NotFoundException {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (!reviewOptional.isPresent()) {
            throw new NotFoundException("Review not found by id: " + reviewId);
        }

        Review existingReview = reviewOptional.get();

        if (!existingReview.getUserId().equals(userData.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized to update this review");
        }

        existingReview.setRating(request.getRating());
        existingReview.setComment(request.getComment());
        existingReview.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(existingReview);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(updatedReview.getId());
        reviewDto.setProductId(updatedReview.getProductId());
        reviewDto.setUserId(updatedReview.getUserId());
        reviewDto.setRating(updatedReview.getRating());
        reviewDto.setComment(updatedReview.getComment());
        reviewDto.setCreatedAt(updatedReview.getCreatedAt());
        reviewDto.setUpdatedAt(updatedReview.getUpdatedAt());

        return reviewDto;
    }

    @Override
    public void deleteReview(String reviewId, UserData userData) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (!reviewOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found by id: " + reviewId);
        }

        Review existingReview = reviewOptional.get();

        if (!existingReview.getUserId().equals(userData.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized to delete this review");
        }

        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewDto> getReviewsByProduct(String productId) {
        List<Review> reviewList = reviewRepository.findByProductId(productId);
        return reviewList.stream().map(review -> {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setReviewId(review.getId());
            reviewDto.setProductId(review.getProductId());
            reviewDto.setUserId(review.getUserId());
            reviewDto.setRating(review.getRating());
            reviewDto.setComment(review.getComment());
            reviewDto.setCreatedAt(review.getCreatedAt());
            reviewDto.setUpdatedAt(review.getUpdatedAt());
            return reviewDto;
        }).toList();
    }

    @Override
    public ReviewDto getReviewById(String reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found by id: " + reviewId);
        }

        Review review = reviewOptional.get();
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(review.getId());
        reviewDto.setProductId(review.getProductId());
        reviewDto.setUserId(review.getUserId());
        reviewDto.setRating(review.getRating());
        reviewDto.setComment(review.getComment());
        reviewDto.setCreatedAt(review.getCreatedAt());
        reviewDto.setUpdatedAt(review.getUpdatedAt());

        return reviewDto;
    }
}
