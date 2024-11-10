package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.TrackingStatusDto;
import dev.eshan.orderservice.dtos.UserDetails;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.services.interfaces.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public OrderDto createOrder(@RequestParam String userId, UserDetails userDetails) {
        try {
            return orderService.createOrder(userId, userDetails);
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating order");
        }
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable String orderId) throws NotFoundException {
        try {
            return orderService.getOrderById(orderId);
        } catch (Exception e) {
            log.error("Error fetching order", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching order");
        }
    }

    @GetMapping("/history")
    public List<OrderDto> getOrderHistory(@RequestParam String userId) {
        try {
            return orderService.getOrderHistory(userId);
        } catch (Exception e) {
            log.error("Error fetching order history", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching order history");
        }
    }

    @GetMapping("/track/{orderId}")
    public TrackingStatusDto trackOrder(@PathVariable String orderId) throws NotFoundException {
        try {
            return orderService.trackOrder(orderId);
        } catch (Exception e) {
            log.error("Error tracking order", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error tracking order");
        }
    }
}
