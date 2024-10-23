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
    public void addToCart(@RequestBody CartItemDto cartItem) {
        cartService.addToCart(cartItem);
    }

    @PutMapping("/update")
    public void updateCartItem(@RequestBody CartItemDto cartItem) {
        cartService.updateCartItem(cartItem);
    }

    @DeleteMapping("/remove/{itemId}")
    public void removeCartItem(@PathVariable String itemId) {
        cartService.removeCartItem(itemId);
    }

    @PostMapping("/apply-discount")
    public String applyDiscount(@RequestBody DiscountCodeDto discountCode) {
        cartService.applyDiscount(discountCode);
        return "Discount applied successfully";
    }

    @GetMapping("/view")
    public CartDto viewCart() {
        return cartService.viewCart();
    }
}
