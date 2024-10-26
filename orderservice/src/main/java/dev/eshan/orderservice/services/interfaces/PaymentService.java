package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.PaymentRequestDto;
import dev.eshan.orderservice.dtos.PaymentResponseDto;
import dev.eshan.orderservice.dtos.PaymentStatusDto;
import dev.eshan.orderservice.dtos.RetryPaymentDto;

public interface PaymentService {

    PaymentResponseDto processPayment(PaymentRequestDto paymentRequest);

    PaymentStatusDto getPaymentStatus(String paymentId);

    PaymentResponseDto retryPayment(RetryPaymentDto retryPayment);
}
