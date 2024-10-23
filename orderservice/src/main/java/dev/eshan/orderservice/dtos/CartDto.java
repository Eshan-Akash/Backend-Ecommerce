package dev.eshan.orderservice.dtos;

import lombok.Data;
import java.util.List;

@Data
public class CartDto {
    private List<CartItemDto> cartItems;
    private double totalPrice;
    private String discountCode;
    private double discountedPrice;
}
