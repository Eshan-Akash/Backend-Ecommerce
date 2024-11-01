package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.CheckoutRequestDto;
import dev.eshan.orderservice.dtos.CheckoutResponseDto;
import dev.eshan.orderservice.exceptions.NotFoundException;
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
    public CheckoutResponseDto checkout(@RequestBody CheckoutRequestDto checkoutRequest, @RequestParam String userId) {
        return checkoutService.checkout(userId, checkoutRequest);
    }

    @GetMapping("/status")
    public String getCheckoutStatus(@RequestParam String orderId) throws NotFoundException {
        return checkoutService.getCheckoutStatus(orderId);
    }
}
