package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.CheckoutRequestDto;
import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.PaymentRequestDto;
import dev.eshan.orderservice.dtos.PaymentResponseDto;
import dev.eshan.orderservice.services.interfaces.CheckoutService;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Override
    public OrderDto checkout(CheckoutRequestDto checkoutRequest) {
        return null;
    }

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto paymentRequest) {
        return null;
    }

    @Override
    public String getCheckoutStatus(String orderId) {
        return null;
    }
}
