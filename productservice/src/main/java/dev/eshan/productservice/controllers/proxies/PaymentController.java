package dev.eshan.productservice.controllers.proxies;

import dev.eshan.productservice.dtos.proxies.*;
import dev.eshan.productservice.services.impl.PaymentServiceProxyImpl;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
public class PaymentController {

    private final PaymentServiceProxyImpl paymentServiceProxyImpl;

    public PaymentController(PaymentServiceProxyImpl paymentServiceProxyImpl) {
        this.paymentServiceProxyImpl = paymentServiceProxyImpl;
    }

    @PostMapping("/confirm")
    public PaymentConfirmationResponse confirmPayment(@RequestParam String orderId) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            UserDetails userDetails = UserDetails.builder().email(userData.getEmail()).build();
            return paymentServiceProxyImpl.confirmPayment(orderId, userData.getUserId(), userDetails);
        } catch (Exception e) {
            log.error("Error confirming payment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error confirming payment");
        }
    }

    @GetMapping("/status/{paymentId}")
    public PaymentStatusDto getPaymentStatus(@PathVariable String paymentId) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return paymentServiceProxyImpl.getPaymentStatus(paymentId, userData.getUserId());
        } catch (Exception e) {
            log.error("Error getting payment status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting payment status");
        }
    }

    @PostMapping("/retry")
    public PaymentResponseDto retryPayment(@RequestBody RetryPaymentDto retryPayment) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return paymentServiceProxyImpl.retryPayment(retryPayment, userData.getUserId());
        } catch (Exception e) {
            log.error("Error retrying payment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrying payment");
        }
    }
}
