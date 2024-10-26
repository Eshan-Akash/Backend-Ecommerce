package dev.eshan.orderservice.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentReportDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int totalPayments;
    private double totalAmountProcessed;
    private int failedPayments;
}
