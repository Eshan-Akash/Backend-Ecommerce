package dev.eshan.productservice.controllers.proxies;

import dev.eshan.productservice.dtos.proxies.CartDto;
import dev.eshan.productservice.dtos.proxies.CartItemDto;
import dev.eshan.productservice.dtos.proxies.DiscountCodeDto;
import dev.eshan.productservice.services.impl.CartServiceProxyImpl;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartServiceProxyImpl cartServiceProxyImpl;

    public CartController(CartServiceProxyImpl cartServiceProxyImpl) {
        this.cartServiceProxyImpl = cartServiceProxyImpl;
    }

    @PostMapping("/add")
    public CartDto addToCart(@RequestBody CartItemDto cartItem) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("CUSTOMER")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return cartServiceProxyImpl.addToCart(userData.getUserId(), cartItem);
    }

    @PutMapping("/update")
    public CartDto updateCartItem(@RequestBody CartItemDto cartItem) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("CUSTOMER")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return cartServiceProxyImpl.updateCartItem(userData.getUserId(), cartItem);
    }

    @DeleteMapping("/remove/{itemId}")
    public void removeCartItem(@PathVariable String itemId) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("CUSTOMER")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        cartServiceProxyImpl.removeCartItem(userData.getUserId(), itemId);
    }

    @PostMapping("/apply-discount")
    public CartDto applyDiscount(@RequestBody DiscountCodeDto discountCode) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("CUSTOMER")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return cartServiceProxyImpl.applyDiscount(userData.getUserId(), discountCode);
    }

    @GetMapping("/view")
    public CartDto viewCart() throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("CUSTOMER")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return cartServiceProxyImpl.viewCart(userData.getUserId());
    }
}