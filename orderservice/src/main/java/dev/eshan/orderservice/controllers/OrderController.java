package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.TrackingStatusDto;
import dev.eshan.orderservice.services.interfaces.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public OrderDto createOrder() {
        return orderService.createOrder();
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/history")
    public List<OrderDto> getOrderHistory() {
        return orderService.getOrderHistory();
    }

    @GetMapping("/track/{orderId}")
    public TrackingStatusDto trackOrder(@PathVariable String orderId) {
        return orderService.trackOrder(orderId);
    }
}
