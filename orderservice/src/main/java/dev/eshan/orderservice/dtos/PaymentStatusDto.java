package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class PaymentStatusDto {
    private String paymentId;
    private String paymentStatus;
    private String failureReason;
}
