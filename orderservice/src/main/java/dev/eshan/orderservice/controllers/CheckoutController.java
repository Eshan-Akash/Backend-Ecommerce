package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.CheckoutRequestDto;
import dev.eshan.orderservice.dtos.CheckoutResponseDto;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.services.interfaces.CheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/checkout")
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public CheckoutResponseDto checkout(@RequestBody CheckoutRequestDto checkoutRequest, @RequestParam String userId) {
        try {
            return checkoutService.checkout(userId, checkoutRequest);
        } catch (Exception e) {
            log.error("Error checking out", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking out");
        }
    }

    @GetMapping("/status")
    public String getCheckoutStatus(@RequestParam String orderId) throws NotFoundException {
        try {
            return checkoutService.getCheckoutStatus(orderId);
        } catch (Exception e) {
            log.error("Error fetching checkout status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching checkout status");
        }
    }
}
