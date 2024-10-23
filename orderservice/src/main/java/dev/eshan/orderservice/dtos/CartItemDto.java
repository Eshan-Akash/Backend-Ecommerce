package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class CartItemDto {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
}
