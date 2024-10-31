package dev.eshan.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentReportDto {
    private double totalAmount;
    private long successfulPayments;
}
