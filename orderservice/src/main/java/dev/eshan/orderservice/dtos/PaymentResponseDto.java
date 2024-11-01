package dev.eshan.orderservice.dtos;

import dev.eshan.orderservice.models.PaymentStatus;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private String message;
    private String paymentId;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private double amountPaid;
    private String transactionTimestamp;
    private String redirectUrl;
}
