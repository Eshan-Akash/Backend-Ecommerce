package dev.eshan.orderservice.dtos;

import dev.eshan.orderservice.models.OrderItem;
import lombok.Data;

@Data
public class OrderItemDto {
    private String productId;
    private String productName;
    private int quantity;
    private double price;

    public static OrderItemDto from(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}