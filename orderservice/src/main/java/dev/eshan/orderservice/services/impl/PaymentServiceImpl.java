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

        // Check if the transaction is already processed
        if (order.getPayment() != null && order.getPayment().getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Payment already processed for order: " + orderId);
        }

        // Step 2: Set the order status to PENDING
        order.setOrderStatus(OrderStatus.PENDING);

        // Check if the transactionId is already present in the database
        if (order.getPayment() != null && order.getPayment().getTransactionId() != null) {
            throw new IllegalStateException("Payment already initiated for order: " + orderId);
        }

        // Step 3: Generate the payment redirect URL from the payment gateway
        PaymentRedirectResponse redirectResponse = paymentGateway.generatePaymentRedirectURL(orderId, order.getFinalAmount());

        // Step 4: Create a payment record with PENDING status
        Payment payment = new Payment();
        payment.setPaymentGateway("MockGateway");
        payment.setAmount(order.getFinalAmount());
        payment.setPaymentStatus(PaymentStatus.PENDING);  // Initial state before payment is confirmed
        payment.setTransactionId(redirectResponse.getTransactionId());
        payment = paymentRepository.save(payment);

        // Attach the payment to the order
        order.setPayment(payment);
        orderRepository.save(order);

        // Step 5: Return the payment response DTO with the redirect URL
        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setPaymentId(payment.getId());
        responseDto.setPaymentStatus(PaymentStatus.PENDING);
        responseDto.setTransactionId(redirectResponse.getTransactionId());
        responseDto.setAmountPaid(order.getFinalAmount());
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

            return new PaymentConfirmationResponse(order.getId(), OrderStatus.COMPLETED.name(), "Order completed successfully.");
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
    public PaymentStatusDto getPaymentStatus(String paymentId) throws NotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found for ID: " + paymentId));

        // Fetch the current status of the payment from the mock gateway
        PaymentVerificationResponse verificationResponse = paymentGateway.verifyPayment(payment.getTransactionId());

        // Update payment status in the database based on the verification response
        payment.setPaymentStatus(verificationResponse.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentRepository.save(payment);

        return new PaymentStatusDto(payment.getTransactionId(), payment.getPaymentStatus().name());
    }

    @Override
    public PaymentResponseDto retryPayment(RetryPaymentDto retryPayment) throws NotFoundException {
        Order order = orderRepository.findById(retryPayment.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found for ID: " + retryPayment.getOrderId()));

        // Ensure the order status is pending, otherwise return failure response
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Payment retry is allowed only for pending orders.");
        }

        // Generate a new payment redirect URL for retry
        PaymentRedirectResponse redirectResponse = paymentGateway.generatePaymentRedirectURL(order.getId(), order.getFinalAmount());

        // Update payment record in the database with the new transaction ID and status as PENDING
        Payment payment = order.getPayment();
        payment.setTransactionId(redirectResponse.getTransactionId());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        // Return the payment response DTO with the redirect URL
        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setMessage("Payment retry initiated");
        responseDto.setPaymentId(payment.getId());
        responseDto.setPaymentMethod(payment.getPaymentGateway());
        responseDto.setPaymentStatus(PaymentStatus.PENDING);
        responseDto.setTransactionId(redirectResponse.getTransactionId());
        responseDto.setAmountPaid(order.getFinalAmount());
        responseDto.setRedirectUrl(redirectResponse.getRedirectUrl());  // URL to redirect user for payment

        // Return the new payment redirect URL and transaction information
        return responseDto;
    }
}
