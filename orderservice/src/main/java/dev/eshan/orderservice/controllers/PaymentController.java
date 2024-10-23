package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.PaymentRequestDto;
import dev.eshan.orderservice.dtos.PaymentResponseDto;
import dev.eshan.orderservice.dtos.PaymentStatusDto;
import dev.eshan.orderservice.dtos.RetryPaymentDto;
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
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto paymentRequest) {
        return paymentService.processPayment(paymentRequest);
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
