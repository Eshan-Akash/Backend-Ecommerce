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
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ReviewDto updateReview(String reviewId, CreateReviewDto request, UserData userData) {
        return null;
    }

    @Override
    public void deleteReview(String reviewId, UserData userData) {

    }

    @Override
    public List<ReviewDto> getReviewsByProduct(String productId) {
        return null;
    }

    @Override
    public ReviewDto getReviewById(String reviewId) {
        return null;
    }
}
