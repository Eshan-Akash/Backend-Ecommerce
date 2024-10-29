package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.dtos.TrackingStatusDto;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.models.Order;
import dev.eshan.orderservice.models.OrderItem;
import dev.eshan.orderservice.models.OrderStatus;
import dev.eshan.orderservice.repositories.OrderRepository;
import dev.eshan.orderservice.services.interfaces.CartService;
import dev.eshan.orderservice.services.interfaces.OrderService;
import dev.eshan.orderservice.services.interfaces.PaymentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentService paymentService;
    @PersistenceContext
    private EntityManager entityManager;

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
        order.setOrderStatus(OrderStatus.INIT);
        order.setShippingAddress("ABC");

        // Convert cart items to order items and attach to the order
        List<OrderItem> orderItems = cartDto.getCartItemDtoList().stream()
                .map(cartItem -> OrderItem.of(cartItem))
                .peek(item -> item.setOrder(order))
                .collect(Collectors.toList());
        order.setOrderItemList(orderItems);

        // Save the order
        Order savedOrder = orderRepository.save(order);
        entityManager.flush();
        entityManager.refresh(savedOrder);

        // Clear the cart after order creation
        cartService.clearCart(userId);

        // Map to OrderDto to return the order information
        return OrderDto.from(savedOrder);
    }

    @Override
    public OrderDto getOrderById(String orderId) throws NotFoundException {
        // Retrieve the order from the repository
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));

        // Convert the order to OrderDto
        return OrderDto.from(order);
    }

    @Override
    public List<OrderDto> getOrderHistory(String userId) {
        // Fetch orders for a specific user
        List<Order> orders = orderRepository.findByUserId(userId);

        // Convert the list of Order entities to a list of OrderDto
        return orders.stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public TrackingStatusDto trackOrder(String orderId) throws NotFoundException {
        // Retrieve the order from the repository
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));

        // Map order details to TrackingStatusDto
        TrackingStatusDto trackingStatus = new TrackingStatusDto();
        trackingStatus.setOrderId(order.getId());
        trackingStatus.setOrderStatus(order.getOrderStatus().name());
        trackingStatus.setCreatedAt(order.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        // If applicable, set estimated delivery time (optional)
        if (order.getOrderStatus() == OrderStatus.SHIPPED) {
            trackingStatus.setEstimatedDeliveryDate(calculateEstimatedDelivery(order.getCreatedAt()));
        }

        return trackingStatus;
    }

    private String calculateEstimatedDelivery(Timestamp createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return createdAt.toLocalDateTime().plusDays(5).format(formatter);
    }
}
