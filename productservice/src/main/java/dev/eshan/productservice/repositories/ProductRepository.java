package dev.eshan.productservice.repositories;

import dev.eshan.productservice.models.Category;
import dev.eshan.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByCategoryIn(List<Category> categories);
    @Query("SELECT p FROM Product p WHERE (p.stockLevel < p.lowStockThreshold)")
    List<Product> findLowStockProducts();
}
