package dev.eshan.productservice.controllers.proxies;


import dev.eshan.productservice.dtos.proxies.OrderDto;
import dev.eshan.productservice.dtos.proxies.TrackingStatusDto;
import dev.eshan.productservice.dtos.proxies.UserDetails;
import dev.eshan.productservice.services.impl.OrderServiceProxyImpl;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import static dev.eshan.productservice.utils.Utils.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderServiceProxyImpl orderServiceProxyImpl;

    public OrderController(OrderServiceProxyImpl orderServiceProxyImpl) {
        this.orderServiceProxyImpl = orderServiceProxyImpl;
    }

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody UserDetails userDetails) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return orderServiceProxyImpl.createOrder(userData.getUserId(), userDetails);
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
        }
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable String orderId) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return orderServiceProxyImpl.getOrderById(orderId, userData.getUserId());
        } catch (Exception e) {
            log.error("Error getting order: {} Error Message: {}", orderId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
        }
    }

    @GetMapping("/history")
    public List<OrderDto> getOrderHistory() throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return orderServiceProxyImpl.getOrderHistory(userData.getUserId());
        } catch (Exception e) {
            log.error("Error getting order history: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
        }
    }

    @GetMapping("/track/{orderId}")
    public TrackingStatusDto trackOrder(@PathVariable String orderId) throws IOException {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserData userData = Utils.createUserDataFromToken(jwt);
            return orderServiceProxyImpl.trackOrder(orderId, userData.getUserId());
        } catch (Exception e) {
            log.error("Error tracking order: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
        }
    }
}