package dev.eshan.orderservice.repositories;

import dev.eshan.orderservice.models.Payment;
import dev.eshan.orderservice.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    // Find payments between specific dates
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") Timestamp startDate,
                                           @Param("endDate") Timestamp endDate);

    // Count total payments (in case needed in future)
    long count();

    // Count payments by status
    long countByPaymentStatus(PaymentStatus status);

    // Sum of all successful payment amounts between dates
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = :status AND p.createdAt BETWEEN :startDate AND :endDate")
    Double sumAmountByStatusAndDateRange(@Param("status") PaymentStatus status,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    boolean existsByTransactionId(String transactionId);

}
