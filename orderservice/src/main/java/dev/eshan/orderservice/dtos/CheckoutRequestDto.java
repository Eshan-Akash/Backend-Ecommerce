package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class CheckoutRequestDto {
    private String paymentMethod;
    private String shippingAddress;
}
