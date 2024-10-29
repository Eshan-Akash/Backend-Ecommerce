package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.networks.PaymentGateway;
import dev.eshan.orderservice.networks.PaymentRedirectResponse;
import dev.eshan.orderservice.networks.PaymentVerificationResponse;
import dev.eshan.orderservice.models.Order;
import dev.eshan.orderservice.models.OrderStatus;
import dev.eshan.orderservice.models.Payment;
import dev.eshan.orderservice.models.PaymentStatus;
import dev.eshan.orderservice.repositories.OrderRepository;
import dev.eshan.orderservice.repositories.PaymentRepository;
import dev.eshan.orderservice.services.interfaces.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository, PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public PaymentResponseDto processPayment(String orderId) throws NotFoundException {
        // Step 1: Retrieve the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));

        // Step 2: Set the order status to PENDING
        order.setOrderStatus(OrderStatus.PENDING);

        // Step 3: Create a payment record with PENDING status
        Payment payment = new Payment();
        payment.setPaymentGateway("MockGateway");
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.PENDING);  // Initial state before payment is confirmed
        payment = paymentRepository.save(payment);

        // Attach the payment to the order
        order.setPayment(payment);
        orderRepository.save(order);

        // Step 4: Generate the payment redirect URL from the payment gateway
        PaymentRedirectResponse redirectResponse = paymentGateway.generatePaymentRedirectURL(orderId, order.getTotalAmount());

        // Step 5: Return the payment response DTO with the redirect URL
        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setPaymentId(payment.getId());
        responseDto.setPaymentStatus(PaymentStatus.PENDING);
        responseDto.setTransactionId(redirectResponse.getTransactionId());
        responseDto.setAmountPaid(order.getTotalAmount());
        responseDto.setRedirectUrl(redirectResponse.getRedirectUrl());  // URL to redirect user for payment

        return responseDto;
    }

    @Override
    public PaymentConfirmationResponse confirmPayment(String orderId) throws NotFoundException {
        // Step 1: Retrieve the order and payment information
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        Payment payment = order.getPayment();
        if (payment == null) {
            throw new IllegalStateException("Payment details not found for order: " + orderId);
        }

        // Step 2: Verify payment status with the payment gateway
        PaymentVerificationResponse verificationResponse = paymentGateway.verifyPayment(payment.getTransactionId());

        if (verificationResponse.isSuccess()) {
            // Step 3: Update payment and order status to SUCCESS
            payment.setPaymentStatus(PaymentStatus.SUCCESS); // Assuming PaymentStatus is an enum with values SUCCESS, FAILED, etc.
            order.setOrderStatus(OrderStatus.COMPLETED);

            // Save the updated payment and order
            paymentRepository.save(payment);
            orderRepository.save(order);

            return new PaymentConfirmationResponse(order.getId(), "SUCCESS", "Order completed successfully.");
        } else {
            // Step 4: Update payment and order status to FAILED if payment verification failed
            payment.setPaymentStatus(PaymentStatus.FAILED);
            order.setOrderStatus(OrderStatus.PENDING); // Optionally keep the order as PENDING for retry

            paymentRepository.save(payment);
            orderRepository.save(order);

            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment verification failed");
        }
    }

    @Override
    public PaymentStatusDto getPaymentStatus(String paymentId) {
        return null;
    }

    @Override
    public PaymentResponseDto retryPayment(RetryPaymentDto retryPayment) {
        return null;
    }
}
