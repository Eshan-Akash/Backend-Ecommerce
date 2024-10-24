package dev.eshan.productservice.repositories;

import dev.eshan.productservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByProductId(String productId);

    List<Review> findByUserId(String userId);
}
