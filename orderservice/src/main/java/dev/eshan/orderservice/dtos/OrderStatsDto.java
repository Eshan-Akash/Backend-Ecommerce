package dev.eshan.orderservice.dtos;

import lombok.Data;

@Data
public class OrderStatsDto {
    private double averageOrderValue;
    private int totalOrders;
    private double totalRevenue;
}
