package dev.eshan.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentReportDto {
    private long totalPayments;
    private double totalAmount;
    private long successfulPayments;
    private double totalSuccessfulAmount;
}
