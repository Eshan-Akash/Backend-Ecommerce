package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.CartItemDto;
import dev.eshan.orderservice.dtos.DiscountCodeDto;

public interface CartService {

    CartDto addToCart(String userId, CartItemDto cartItem);

    CartDto updateCartItem(String userId, CartItemDto cartItem);

    void removeCartItem(String userId, String itemId);

    CartDto applyDiscount(String userId, DiscountCodeDto discountCode);

    CartDto viewCart(String userId);
}
