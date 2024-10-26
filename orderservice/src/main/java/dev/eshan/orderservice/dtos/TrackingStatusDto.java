package dev.eshan.orderservice.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrackingStatusDto {
    private String orderId;
    private String trackingNumber;
    private String shippingStatus;
    private LocalDateTime estimatedDeliveryDate;
}
