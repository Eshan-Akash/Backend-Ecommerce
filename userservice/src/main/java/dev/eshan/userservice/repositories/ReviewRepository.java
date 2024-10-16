package dev.eshan.userservice.repositories;

import dev.eshan.userservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    // Find reviews by product ID
    List<Review> findByProductId(String productId);

    // Find reviews by user ID
    List<Review> findByUserId(String userId);
}
