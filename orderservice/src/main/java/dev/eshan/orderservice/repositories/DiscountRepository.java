package dev.eshan.orderservice.repositories;

import dev.eshan.orderservice.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    Discount findByCode(String code);
}
