package dev.eshan.orderservice.repositories;

import dev.eshan.orderservice.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    // Custom query methods can be added here
}
