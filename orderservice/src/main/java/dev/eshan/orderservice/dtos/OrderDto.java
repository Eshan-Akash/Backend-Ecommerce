package dev.eshan.orderservice.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private String orderId;
    private String userId;
    private List<CartItemDto> orderItems;
    private String orderStatus;
    private double totalAmount;
    private String paymentStatus;
    private String shippingAddress;
    private LocalDateTime orderDate;
}
