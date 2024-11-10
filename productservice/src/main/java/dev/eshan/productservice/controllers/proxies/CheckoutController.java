package dev.eshan.productservice.controllers.proxies;

import dev.eshan.productservice.dtos.proxies.CheckoutRequestDto;
import dev.eshan.productservice.dtos.proxies.PaymentResponseDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.impl.CheckoutServiceProxyImpl;
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
@RequestMapping("/api/v1/checkout")
@Slf4j
public class CheckoutController {

    private final CheckoutServiceProxyImpl checkoutServiceProxyImpl;

    public CheckoutController(CheckoutServiceProxyImpl checkoutServiceProxyImpl) {
        this.checkoutServiceProxyImpl = checkoutServiceProxyImpl;
    }

    @PostMapping
    public PaymentResponseDto checkout(@RequestBody CheckoutRequestDto checkoutRequest) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return checkoutServiceProxyImpl.checkout(userData.getUserId(), checkoutRequest);
        } catch (Exception e) {
            log.error("Error while checking out", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while checking out");
        }
    }

    @GetMapping("/status")
    public String getCheckoutStatus(@RequestParam String orderId) throws NotFoundException, IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return checkoutServiceProxyImpl.getCheckoutStatus(orderId, userData.getUserId());
        } catch (Exception e) {
            log.error("Error while getting checkout status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting checkout status");
        }
    }
}
