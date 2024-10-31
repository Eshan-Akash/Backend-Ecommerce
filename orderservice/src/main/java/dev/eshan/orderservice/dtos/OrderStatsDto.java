package dev.eshan.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatsDto {
    private int totalOrders;
    private int pendingOrders;
    private int completedOrders;
    private int canceledOrders;
}
