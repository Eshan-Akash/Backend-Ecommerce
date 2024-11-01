package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.TrackingStatusDto;
import dev.eshan.orderservice.dtos.UserDetails;
import dev.eshan.orderservice.exceptions.NotFoundException;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(String userId, UserDetails userDetails);

    OrderDto getOrderById(String orderId) throws NotFoundException;

    List<OrderDto> getOrderHistory(String userId);

    TrackingStatusDto trackOrder(String orderId) throws NotFoundException;
}
