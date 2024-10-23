package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class PaymentResponseDto {
    private String paymentId;
    private String paymentStatus;
    private String transactionId;
    private double amount;
}
