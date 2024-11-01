package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.exceptions.NotFoundException;

public interface PaymentService {

    PaymentResponseDto processPayment(String orderId) throws NotFoundException;

    PaymentConfirmationResponse confirmPayment(String orderId) throws NotFoundException;

    PaymentStatusDto getPaymentStatus(String paymentId) throws NotFoundException;

    PaymentResponseDto retryPayment(RetryPaymentDto retryPayment) throws NotFoundException;
}
