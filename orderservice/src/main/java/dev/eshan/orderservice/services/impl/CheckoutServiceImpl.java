package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.CheckoutRequestDto;
import dev.eshan.orderservice.dtos.CheckoutResponseDto;
import dev.eshan.orderservice.dtos.OrderDto;
import dev.eshan.orderservice.exceptions.NotFoundException;
import dev.eshan.orderservice.models.OrderStatus;
import dev.eshan.orderservice.services.interfaces.CartService;
import dev.eshan.orderservice.services.interfaces.CheckoutService;
import dev.eshan.orderservice.services.interfaces.OrderService;
import dev.eshan.orderservice.services.interfaces.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public CheckoutServiceImpl(CartService cartService, OrderService orderService, PaymentService paymentService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @Override
    public CheckoutResponseDto checkout(String userId, CheckoutRequestDto checkoutRequest) {
        CartDto cart = cartService.viewCart(userId);
        if (cart == null || cart.getCartItemDtoList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        // Create the order
        OrderDto order = orderService.createOrder(userId);

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
