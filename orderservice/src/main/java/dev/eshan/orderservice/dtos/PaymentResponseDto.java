package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class PaymentResponseDto {
    private boolean success;
    private String message;
    private String paymentId;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private double amountPaid;
    private String transactionTimestamp;
}
