package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.services.interfaces.CheckoutService;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Override
    public OrderDto checkout(String userId, CheckoutRequestDto checkoutRequest, CartDto cartDto) {
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
