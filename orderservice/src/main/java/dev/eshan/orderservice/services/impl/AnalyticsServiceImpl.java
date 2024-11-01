package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.OrderReportDto;
import dev.eshan.orderservice.dtos.OrderStatsDto;
import dev.eshan.orderservice.dtos.PaymentReportDto;
import dev.eshan.orderservice.models.Order;
import dev.eshan.orderservice.models.OrderStatus;
import dev.eshan.orderservice.models.Payment;
import dev.eshan.orderservice.models.PaymentStatus;
import dev.eshan.orderservice.repositories.OrderRepository;
import dev.eshan.orderservice.repositories.PaymentRepository;
import dev.eshan.orderservice.services.interfaces.AnalyticsService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public AnalyticsServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public OrderReportDto getOrderAnalytics(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Timestamp start = startDate != null ? Timestamp.valueOf(LocalDate.parse(startDate, formatter).atStartOfDay()) : null;
        Timestamp end = endDate != null ? Timestamp.valueOf(LocalDate.parse(endDate, formatter).atTime(23, 59, 59)) : null;

        List<Order> orders;
        if (start != null && end != null) {
            orders = orderRepository.findOrdersBetweenDates(start, end);
        } else {
            orders = orderRepository.findAll();
        }

        double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        int totalOrders = orders.size();

        return new OrderReportDto(totalOrders, totalRevenue);
    }

    @Override
    public PaymentReportDto getPaymentAnalytics(String startDate, String endDate) {
        // Parse the startDate and endDate to Timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Timestamp start = startDate != null ? Timestamp.valueOf(LocalDate.parse(startDate, formatter).atStartOfDay()) : null;
        Timestamp end = endDate != null ? Timestamp.valueOf(LocalDate.parse(endDate, formatter).atTime(23, 59, 59)) : null;

        List<Payment> payments;
        if (start != null && end != null) {
            payments = paymentRepository.findPaymentsBetweenDates(start, end);
        } else {
            payments = paymentRepository.findAll();
        }

        // Calculate analytics (e.g., total payments, successful payments, etc.)
        long totalPayment = payments.size();
        double totalAmount = payments.stream().mapToDouble(Payment::getAmount).sum();
        double totalSuccessfulAmount = payments.stream()
                .filter(payment -> payment.getPaymentStatus() == PaymentStatus.SUCCESS)
                .mapToDouble(Payment::getAmount)
                .sum();
        long successfulPayments = payments.stream()
                .filter(payment -> payment.getPaymentStatus() == PaymentStatus.SUCCESS)
                .count();

        return new PaymentReportDto(totalPayment, totalAmount, successfulPayments, totalSuccessfulAmount);
    }

    @Override
    public OrderStatsDto getOrderStats() {
        // Retrieve overall order statistics efficiently using repository methods
        int totalOrders = (int) orderRepository.count();
        int pendingOrders = (int) orderRepository.countByOrderStatus(OrderStatus.PENDING);
        int completedOrders = (int) orderRepository.countByOrderStatus(OrderStatus.COMPLETED);
        int canceledOrders = (int) orderRepository.countByOrderStatus(OrderStatus.CANCELED);

        return new OrderStatsDto(totalOrders, pendingOrders, completedOrders, canceledOrders);
    }
}