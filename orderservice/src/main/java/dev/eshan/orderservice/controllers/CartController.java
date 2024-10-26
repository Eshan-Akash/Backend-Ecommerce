package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.CartItemDto;
import dev.eshan.orderservice.dtos.DiscountCodeDto;
import dev.eshan.orderservice.services.interfaces.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public CartDto addToCart(@RequestBody CartItemDto cartItem, @RequestParam String userId) {
        return cartService.addToCart(userId, cartItem);
    }

    @PutMapping("/update")
    public CartDto updateCartItem(@RequestBody CartItemDto cartItem, @RequestParam String userId) {
        return cartService.updateCartItem(userId, cartItem);
    }

    @DeleteMapping("/remove/{itemId}")
    public void removeCartItem(@PathVariable String itemId, @RequestParam String userId) {
        cartService.removeCartItem(userId, itemId);
    }

    @PostMapping("/apply-discount")
    public CartDto applyDiscount(@RequestBody DiscountCodeDto discountCode, @RequestParam String userId) {
        return cartService.applyDiscount(userId, discountCode);
    }

    @GetMapping("/view")
    public CartDto viewCart(@RequestParam String userId) {
        return cartService.viewCart(userId);
    }
}
