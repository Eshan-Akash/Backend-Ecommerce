package dev.eshan.orderservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartDto {
    private String userId;
    private List<CartItemDto> cartItemDtoList;
    private double totalOriginalPrice;
    private String appliedDiscountCode;
    private double discountPrice = 0.0;
    private double finalPrice;

    public CartDto(String userId, List<CartItemDto> cartItemDtoList, double totalOriginalPrice, String appliedDiscountCode, double discountPrice) {
        this.userId = userId;
        this.cartItemDtoList = cartItemDtoList;
        this.totalOriginalPrice = totalOriginalPrice;
        this.appliedDiscountCode = appliedDiscountCode;
        this.discountPrice = discountPrice;
        this.finalPrice = totalOriginalPrice - discountPrice;
    }
}
