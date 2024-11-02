package dev.eshan.productservice.dtos.proxies;

import lombok.Data;

@Data
public class TrackingStatusDto {
    private String orderId;
    private String trackingNumber; // Nullable; may be null if not shipped yet
    private String orderStatus; // Represents overall order status (e.g., PENDING, SHIPPED, DELIVERED)
    private String createdAt; // Order creation time
    private String updatedAt; // Last updated time (for status updates)
    private String estimatedDeliveryDate;
}
