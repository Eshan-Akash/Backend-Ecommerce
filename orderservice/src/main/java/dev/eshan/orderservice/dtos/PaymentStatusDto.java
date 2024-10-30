package dev.eshan.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentStatusDto {
    private String transactionId;
    private String paymentStatus;
}
