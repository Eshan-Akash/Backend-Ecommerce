package dev.eshan.productservice.dtos.proxies;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentConfirmationResponse {
    private String orderId;
    private String status;
    private String message;
}
