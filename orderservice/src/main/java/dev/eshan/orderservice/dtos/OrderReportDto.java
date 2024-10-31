package dev.eshan.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderReportDto {
    private int totalOrders;
    private double totalRevenue;
}
