package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.services.interfaces.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public PaymentResponseDto processPayment(@RequestParam String orderId) throws NotFoundException {
        return paymentService.processPayment(orderId);
    }

    @PostMapping("/confirm")
    public PaymentConfirmationResponse confirmPayment(@RequestParam String orderId) throws NotFoundException {
        return paymentService.confirmPayment(orderId);
    }

    @GetMapping("/status/{paymentId}")
    public PaymentStatusDto getPaymentStatus(@PathVariable String paymentId) {
        return paymentService.getPaymentStatus(paymentId);
    }

    @PostMapping("/retry")
    public PaymentResponseDto retryPayment(@RequestBody RetryPaymentDto retryPayment) {
        return paymentService.retryPayment(retryPayment);
    }
}
