package dev.eshan.productservice.dtos.proxies;

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
