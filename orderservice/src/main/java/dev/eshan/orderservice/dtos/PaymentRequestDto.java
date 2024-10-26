package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private String orderId;
    private String paymentMethod;
    private double amount;
}
