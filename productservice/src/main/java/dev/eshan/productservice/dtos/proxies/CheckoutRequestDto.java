package dev.eshan.productservice.dtos.proxies;

import lombok.Data;

@Data
public class CheckoutRequestDto {
    private String paymentMethod;
    private String shippingAddress;
}
