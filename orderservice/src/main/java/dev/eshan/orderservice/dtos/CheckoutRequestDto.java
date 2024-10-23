package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class CheckoutRequestDto {
    private String userId;
    private String paymentMethod;
    private String shippingAddress;
}
