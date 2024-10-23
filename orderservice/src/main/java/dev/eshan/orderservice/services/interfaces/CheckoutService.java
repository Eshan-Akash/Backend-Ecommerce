package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.CheckoutRequestDto;
import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.PaymentRequestDto;
import dev.eshan.orderservice.dtos.PaymentResponseDto;

public interface CheckoutService {

    OrderDto checkout(CheckoutRequestDto checkoutRequest);

    PaymentResponseDto processPayment(PaymentRequestDto paymentRequest);

    String getCheckoutStatus(String orderId);
}
