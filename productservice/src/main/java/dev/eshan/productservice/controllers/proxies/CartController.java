package dev.eshan.productservice.controllers.proxies;

import dev.eshan.productservice.dtos.proxies.CartDto;
import dev.eshan.productservice.dtos.proxies.CartItemDto;
import dev.eshan.productservice.dtos.proxies.DiscountCodeDto;
import dev.eshan.productservice.services.impl.CartServiceProxyImpl;
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
@RequestMapping("/api/v1/cart")
@Slf4j
public class CartController {

    private final CartServiceProxyImpl cartServiceProxyImpl;

    public CartController(CartServiceProxyImpl cartServiceProxyImpl) {
        this.cartServiceProxyImpl = cartServiceProxyImpl;
    }

    @PostMapping("/add")
    public CartDto addToCart(@RequestBody CartItemDto cartItem) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            if (!userData.getUserRole().contains("CUSTOMER")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
            }
            return cartServiceProxyImpl.addToCart(userData.getUserId(), cartItem);
        } catch (Exception e) {
            log.error("Error adding item to cart: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding item to cart");
        }
    }

    @PutMapping("/update")
    public CartDto updateCartItem(@RequestBody CartItemDto cartItem) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            if (!userData.getUserRole().contains("CUSTOMER")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
            }
            return cartServiceProxyImpl.updateCartItem(userData.getUserId(), cartItem);
        } catch (Exception e) {
            log.error("Error updating item in cart: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating item in cart");
        }
    }

    @DeleteMapping("/remove/{itemId}")
    public void removeCartItem(@PathVariable String itemId) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            if (!userData.getUserRole().contains("CUSTOMER")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
            }
            cartServiceProxyImpl.removeCartItem(userData.getUserId(), itemId);
        } catch (Exception e) {
            log.error("Error removing item from cart: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error removing item from cart");
        }
    }

    @PostMapping("/apply-discount")
    public CartDto applyDiscount(@RequestBody DiscountCodeDto discountCode) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            if (!userData.getUserRole().contains("CUSTOMER")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
            }
            return cartServiceProxyImpl.applyDiscount(userData.getUserId(), discountCode);
        } catch (Exception e) {
            log.error("Error applying discount: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error applying discount");
        }
    }

    @GetMapping("/view")
    public CartDto viewCart() throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            if (!userData.getUserRole().contains("CUSTOMER")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
            }
            return cartServiceProxyImpl.viewCart(userData.getUserId());
        } catch (Exception e) {
            log.error("Error viewing cart: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error viewing cart");
        }
    }
}