package dev.eshan.orderservice.dtos;

import dev.eshan.orderservice.models.Order;
import dev.eshan.orderservice.models.OrderStatus;
import dev.eshan.orderservice.models.PaymentStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private String orderId;
    private String userId;
    private double finalAmount;
    private double totalOriginalAmount;
    private List<OrderItemDto> orderItemList;
    private OrderStatus orderStatus;
    private Timestamp createdAt;
    private PaymentStatus paymentStatus;
    private String shippingAddress;

    /**
     * Converts an Order entity to an OrderDto.
     *
     * @param order the Order entity to convert
     * @return the converted OrderDto
     */
    public static OrderDto from(Order order) {
        OrderDto orderDto = new OrderDto();

        orderDto.setOrderId(order.getId());
        orderDto.setUserId(order.getUserId());
        orderDto.setFinalAmount(order.getFinalAmount());
        orderDto.setTotalOriginalAmount(order.getTotalAmount());

        orderDto.setOrderItemList(order.getOrderItemList().stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList()));

        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setCreatedAt(order.getCreatedAt());

        if (order.getPayment() != null) {
            orderDto.setPaymentStatus(order.getPayment().getPaymentStatus());
        } else {
            orderDto.setPaymentStatus(null);
        }

        orderDto.setShippingAddress(order.getShippingAddress());

        return orderDto;
    }
}