package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class RetryPaymentDto {
    private String paymentId;
    private String paymentMethod;
}
