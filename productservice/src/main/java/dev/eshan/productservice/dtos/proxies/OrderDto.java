package dev.eshan.productservice.dtos.proxies;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private String orderId;
    private String userId;
    private double finalAmount;
    private List<OrderItemDto> orderItemList;
    private OrderStatus orderStatus;
    private Timestamp createdAt;
    private PaymentStatus paymentStatus;
    private String shippingAddress;
}