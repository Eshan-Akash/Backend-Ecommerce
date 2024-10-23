package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.CheckoutRequestDto;
import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.PaymentRequestDto;
import dev.eshan.orderservice.dtos.PaymentResponseDto;
import dev.eshan.orderservice.services.interfaces.CheckoutService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public OrderDto checkout(@RequestBody CheckoutRequestDto checkoutRequest) {
        return checkoutService.checkout(checkoutRequest);
    }

    @PostMapping("/payment")
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto paymentRequest) {
        return checkoutService.processPayment(paymentRequest);
    }

    @GetMapping("/status")
    public String getCheckoutStatus(@RequestParam String orderId) {
        return checkoutService.getCheckoutStatus(orderId);
    }
}
