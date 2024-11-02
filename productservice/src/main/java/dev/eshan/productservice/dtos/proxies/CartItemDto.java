package dev.eshan.productservice.dtos.proxies;

import lombok.Data;

@Data
public class CartItemDto {
    private String itemId;
    private String productId;
    private String productName;
    private int quantity;
    private double pricePerUnit;
    private double totalPrice;
}
