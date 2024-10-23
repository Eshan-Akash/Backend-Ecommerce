package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.TrackingStatusDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder();

    OrderDto getOrderById(String orderId);

    List<OrderDto> getOrderHistory();

    TrackingStatusDto trackOrder(String orderId);
}
