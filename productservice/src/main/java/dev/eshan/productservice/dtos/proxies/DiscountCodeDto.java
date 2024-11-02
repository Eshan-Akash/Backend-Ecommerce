package dev.eshan.productservice.dtos.proxies;

import lombok.Data;

@Data
public class DiscountCodeDto {
    private String code;
    private double discountPercentage;
}
