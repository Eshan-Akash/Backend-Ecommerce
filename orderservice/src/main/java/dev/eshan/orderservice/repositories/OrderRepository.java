package dev.eshan.orderservice.repositories;

import dev.eshan.orderservice.models.Order;
import dev.eshan.orderservice.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    long count();

    long countByOrderStatus(OrderStatus status);
}
