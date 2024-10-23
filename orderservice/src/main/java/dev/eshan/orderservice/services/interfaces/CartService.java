package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.CartItemDto;
import dev.eshan.orderservice.dtos.DiscountCodeDto;

public interface CartService {

    void addToCart(CartItemDto cartItem);

    void updateCartItem(CartItemDto cartItem);

    void removeCartItem(String itemId);

    void applyDiscount(DiscountCodeDto discountCode);

    CartDto viewCart();
}
