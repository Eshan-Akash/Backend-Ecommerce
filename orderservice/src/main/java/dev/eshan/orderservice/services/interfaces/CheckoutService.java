package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.models.Cart;

public interface CheckoutService {

    OrderDto checkout(String userId, CheckoutRequestDto checkoutRequest, CartDto cartDto);

    PaymentResponseDto processPayment(PaymentRequestDto paymentRequest);

    String getCheckoutStatus(String orderId);
}
