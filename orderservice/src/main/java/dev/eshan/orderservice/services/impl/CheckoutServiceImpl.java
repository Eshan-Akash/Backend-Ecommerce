package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.*;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.models.OrderStatus;
import dev.eshan.orderservice.services.interfaces.CartService;
import dev.eshan.orderservice.services.interfaces.CheckoutService;
import dev.eshan.orderservice.services.interfaces.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutServiceImpl(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Override
    public CheckoutResponseDto checkout(String userId, CheckoutRequestDto checkoutRequest) {
        CartDto cart = cartService.viewCart(userId);
        if (cart == null || cart.getCartItemDtoList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        UserDetails userDetails = UserDetails.builder()
                .address(checkoutRequest.getShippingAddress())
                .build();
        // Create the order
        OrderDto order = orderService.createOrder(userId, userDetails);

        // Redirect to the payment page with the generated order ID
        String paymentUrl = "/api/v1/payment/process?orderId=" + order.getOrderId();
        return new CheckoutResponseDto(paymentUrl);
    }

    @Override
    public String getCheckoutStatus(String orderId) throws NotFoundException {
        // Find the order by its ID
        OrderDto orderDto = orderService.getOrderById(orderId);

        // Retrieve the order status
        OrderStatus orderStatus = orderDto.getOrderStatus();

        // Convert the order status to a meaningful message
        switch (orderStatus) {
            case INIT:
                return "Your order has been created and is awaiting payment.";
            case PENDING:
                return "Your order is pending. Please complete the payment.";
            case PROCESSING:
                return "Your order is being processed.";
            case COMPLETED:
                return "Your order has been completed and is on its way.";
            case CANCELED:
                return "Your order was canceled.";
            default:
                return "Unknown status. Please contact support.";
        }
    }
}
