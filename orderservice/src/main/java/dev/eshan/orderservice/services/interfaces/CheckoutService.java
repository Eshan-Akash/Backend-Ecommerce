package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.exceptions.NotFoundException;

public interface CheckoutService {

    CheckoutResponseDto checkout(String userId, CheckoutRequestDto checkoutRequest);

    String getCheckoutStatus(String orderId) throws NotFoundException;
}
