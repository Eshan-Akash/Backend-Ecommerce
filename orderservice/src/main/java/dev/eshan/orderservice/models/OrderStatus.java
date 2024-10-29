package dev.eshan.orderservice.models;

public enum OrderStatus {
    INIT,
    PENDING,       // Order is placed but not yet processed
    PROCESSING,    // Order is being processed
    SHIPPED,       // Order has been shipped to the customer
    DELIVERED,     // Order has been delivered to the customer
    COMPLETED,     // Order has been fully completed
    CANCELED,      // Order has been canceled by the customer or admin
    RETURNED,      // Order has been returned by the customer
    REFUNDED,      // Refund has been issued for the order
    FAILED,        // Order processing failed
    ON_HOLD        // Order is on hold for any reason (e.g., awaiting payment, stock)

}
