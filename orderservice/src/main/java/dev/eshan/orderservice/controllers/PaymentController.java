package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.services.interfaces.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public PaymentResponseDto processPayment(@RequestParam String orderId) throws NotFoundException {
        try {
            return paymentService.processPayment(orderId);
        } catch (NotFoundException e) {
            log.error("Error processing payment", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error processing payment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing payment");
        }
    }

    @PostMapping("/confirm")
    public PaymentConfirmationResponse confirmPayment(@RequestParam String orderId, @RequestBody UserDetails userDetails) throws NotFoundException {
        try {
            return paymentService.confirmPayment(orderId, userDetails);
        } catch (NotFoundException e) {
            log.error("Error confirming payment", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error confirming payment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error confirming payment");
        }
    }

    @GetMapping("/status/{paymentId}")
    public PaymentStatusDto getPaymentStatus(@PathVariable String paymentId) throws NotFoundException {
        try {
            return paymentService.getPaymentStatus(paymentId);
        } catch (NotFoundException e) {
            log.error("Error fetching payment status", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching payment status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching payment status");
        }
    }

    @PostMapping("/retry")
    public PaymentResponseDto retryPayment(@RequestBody RetryPaymentDto retryPayment) throws NotFoundException {
        try {
            return paymentService.retryPayment(retryPayment);
        } catch (NotFoundException e) {
            log.error("Error retrying payment", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error retrying payment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrying payment");
        }
    }
}
