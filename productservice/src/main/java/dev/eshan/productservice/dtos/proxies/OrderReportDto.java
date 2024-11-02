package dev.eshan.productservice.dtos.proxies;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderReportDto {
    private int totalOrders;
    private double totalRevenue;
}
