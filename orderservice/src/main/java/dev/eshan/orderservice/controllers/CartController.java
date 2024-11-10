package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.CartItemDto;
import dev.eshan.orderservice.dtos.DiscountCodeDto;
import dev.eshan.orderservice.services.interfaces.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/cart")
@Slf4j
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public CartDto addToCart(@RequestBody CartItemDto cartItem, @RequestParam String userId) {
        try {
            return cartService.addToCart(userId, cartItem);
        } catch (Exception e) {
            log.error("Error adding item to cart", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding item to cart");
        }
    }

    @PutMapping("/update")
    public CartDto updateCartItem(@RequestBody CartItemDto cartItem, @RequestParam String userId) {
        try {
            return cartService.updateCartItem(userId, cartItem);
        } catch (Exception e) {
            log.error("Error updating item in cart", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating item in cart");
        }
    }

    @DeleteMapping("/remove/{itemId}")
    public void removeCartItem(@PathVariable String itemId, @RequestParam String userId) {
        try {
            cartService.removeCartItem(userId, itemId);
        } catch (Exception e) {
            log.error("Error removing item from cart", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error removing item from cart");
        }
    }

    @PostMapping("/apply-discount")
    public CartDto applyDiscount(@RequestBody DiscountCodeDto discountCode, @RequestParam String userId) {
        try {
            return cartService.applyDiscount(userId, discountCode);
        } catch (Exception e) {
            log.error("Error applying discount", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error applying discount");
        }
    }

    @GetMapping("/view")
    public CartDto viewCart(@RequestParam String userId) {
        try {
            return cartService.viewCart(userId);
        } catch (Exception e) {
            log.error("Error viewing cart", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error viewing cart");
        }
    }
}
