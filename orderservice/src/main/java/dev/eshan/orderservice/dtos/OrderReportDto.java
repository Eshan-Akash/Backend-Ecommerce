package dev.eshan.orderservice.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderReportDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int totalOrders;
    private double totalRevenue;
}
