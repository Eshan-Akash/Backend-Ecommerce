package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.TrackingStatusDto;
import dev.eshan.orderservice.models.Order;
import dev.eshan.orderservice.models.OrderItem;
import dev.eshan.orderservice.models.OrderStatus;
import dev.eshan.orderservice.repositories.OrderRepository;
import dev.eshan.orderservice.services.interfaces.CartService;
import dev.eshan.orderservice.services.interfaces.OrderService;
import dev.eshan.orderservice.services.interfaces.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentService paymentService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public OrderDto createOrder(String userId) {
        // Retrieve user's cart for the current order
        CartDto cartDto = cartService.viewCart(userId);
        if (cartDto.getCartItemDtoList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart is empty. Cannot create order.");
        }

        // Create a new Order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(cartDto.getFinalPrice());
        order.setOrderStatus(OrderStatus.PENDING);

        // Convert cart items to order items and attach to the order
        List<OrderItem> orderItems = cartDto.getCartItemDtoList().stream()
                .map(cartItem -> OrderItem.of(cartItem))
                .collect(Collectors.toList());
        order.setOrderItemList(orderItems);

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after order creation
        cartService.clearCart(userId);

        // Map to OrderDto to return the order information
        return OrderDto.from(savedOrder);
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
