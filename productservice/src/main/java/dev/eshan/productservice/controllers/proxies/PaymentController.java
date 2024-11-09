package dev.eshan.productservice.controllers.proxies;

import dev.eshan.productservice.dtos.proxies.*;
import dev.eshan.productservice.services.impl.PaymentServiceProxyImpl;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentServiceProxyImpl paymentServiceProxyImpl;

    public PaymentController(PaymentServiceProxyImpl paymentServiceProxyImpl) {
        this.paymentServiceProxyImpl = paymentServiceProxyImpl;
    }

    @PostMapping("/confirm")
    public PaymentConfirmationResponse confirmPayment(@RequestParam String orderId) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        UserDetails userDetails = UserDetails.builder().email(userData.getEmail()).build();
        return paymentServiceProxyImpl.confirmPayment(orderId, userData.getUserId(), userDetails);
    }

    @GetMapping("/status/{paymentId}")
    public PaymentStatusDto getPaymentStatus(@PathVariable String paymentId) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return paymentServiceProxyImpl.getPaymentStatus(paymentId, userData.getUserId());
    }

    @PostMapping("/retry")
    public PaymentResponseDto retryPayment(@RequestBody RetryPaymentDto retryPayment) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return paymentServiceProxyImpl.retryPayment(retryPayment, userData.getUserId());
    }
}
