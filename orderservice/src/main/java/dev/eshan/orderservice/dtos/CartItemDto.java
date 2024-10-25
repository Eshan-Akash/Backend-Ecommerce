package dev.eshan.orderservice.dtos;

import dev.eshan.orderservice.models.CartItem;
import lombok.Data;

@Data
public class CartItemDto {
    private String productId;
    private String productName;
    private int quantity;
    private double pricePerUnit;
    private double totalPrice;

    public static CartItemDto of(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(cartItem.getProductId());
        cartItemDto.setProductName(cartItem.getProductName());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setPricePerUnit(cartItem.getPrice());
        return cartItemDto;
    }
}
