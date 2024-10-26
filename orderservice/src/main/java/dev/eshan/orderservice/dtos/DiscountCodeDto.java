package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class DiscountCodeDto {
    private String code;
    private double discountPercentage;
}
