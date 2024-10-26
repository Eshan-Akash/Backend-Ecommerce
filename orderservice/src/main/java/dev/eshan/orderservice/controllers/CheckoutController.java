package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.services.interfaces.CartService;
import dev.eshan.orderservice.services.interfaces.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CartService cartService;

    public CheckoutController(CheckoutService checkoutService, CartService cartService) {
        this.checkoutService = checkoutService;
        this.cartService = cartService;
    }

    @PostMapping
    public OrderDto checkout(@RequestBody CheckoutRequestDto checkoutRequest, @RequestParam String userId) {
        CartDto cart = cartService.viewCart(userId);
        if (cart == null || cart.getCartItemDtoList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        return checkoutService.checkout(userId, checkoutRequest, cart);
    }

    @PostMapping("/payment")
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto paymentRequest) {
        PaymentResponseDto paymentResponse = checkoutService.processPayment(paymentRequest);
        if (!paymentResponse.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment failed");
        }
        return checkoutService.processPayment(paymentRequest);
    }

    @GetMapping("/status")
    public String getCheckoutStatus(@RequestParam String orderId) {
        return checkoutService.getCheckoutStatus(orderId);
    }
}
