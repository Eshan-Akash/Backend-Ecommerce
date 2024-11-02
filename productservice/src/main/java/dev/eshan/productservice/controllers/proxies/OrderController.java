package dev.eshan.productservice.controllers.proxies;


import dev.eshan.productservice.dtos.proxies.OrderDto;
import dev.eshan.productservice.dtos.proxies.TrackingStatusDto;
import dev.eshan.productservice.dtos.proxies.UserDetails;
import dev.eshan.productservice.services.impl.OrderServiceProxyImpl;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderServiceProxyImpl orderServiceProxyImpl;

    public OrderController(OrderServiceProxyImpl orderServiceProxyImpl) {
        this.orderServiceProxyImpl = orderServiceProxyImpl;
    }

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody UserDetails userDetails) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return orderServiceProxyImpl.createOrder(userData.getUserId(), userDetails);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable String orderId) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return orderServiceProxyImpl.getOrderById(orderId, userData.getUserId());
    }

    @GetMapping("/history")
    public List<OrderDto> getOrderHistory() throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return orderServiceProxyImpl.getOrderHistory(userData.getUserId());
    }

    @GetMapping("/track/{orderId}")
    public TrackingStatusDto trackOrder(@PathVariable String orderId) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        return orderServiceProxyImpl.trackOrder(orderId, userData.getUserId());
    }
}