package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.PaymentRequestDto;
import dev.eshan.orderservice.dtos.PaymentResponseDto;
import dev.eshan.orderservice.dtos.PaymentStatusDto;
import dev.eshan.orderservice.dtos.RetryPaymentDto;
import dev.eshan.orderservice.services.interfaces.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto paymentRequest) {
        return null;
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
