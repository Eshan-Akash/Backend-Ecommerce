package dev.eshan.productservice.controllers.proxies;

import dev.eshan.productservice.dtos.proxies.CheckoutRequestDto;
import dev.eshan.productservice.dtos.proxies.PaymentResponseDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.impl.CheckoutServiceProxyImpl;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutServiceProxyImpl checkoutServiceProxyImpl;

    public CheckoutController(CheckoutServiceProxyImpl checkoutServiceProxyImpl) {
        this.checkoutServiceProxyImpl = checkoutServiceProxyImpl;
    }

    @PostMapping
    public PaymentResponseDto checkout(@RequestBody CheckoutRequestDto checkoutRequest) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return checkoutServiceProxyImpl.checkout(userData.getUserId(), checkoutRequest);
    }

    @GetMapping("/status")
    public String getCheckoutStatus(@RequestParam String orderId) throws NotFoundException, IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return checkoutServiceProxyImpl.getCheckoutStatus(orderId, userData.getUserId());
    }
}
