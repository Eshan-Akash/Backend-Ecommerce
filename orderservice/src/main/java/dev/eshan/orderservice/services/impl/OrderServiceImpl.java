package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.TrackingStatusDto;
import dev.eshan.orderservice.services.interfaces.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderDto createOrder() {
        return null;
    }

    @Override
    public OrderDto getOrderById(String orderId) {
        return null;
    }

    @Override
    public List<OrderDto> getOrderHistory() {
        return null;
    }

    @Override
    public TrackingStatusDto trackOrder(String orderId) {
        return null;
    }
}
