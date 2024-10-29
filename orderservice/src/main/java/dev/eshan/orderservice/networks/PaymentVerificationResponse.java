package dev.eshan.orderservice.networks;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentVerificationResponse {
    private String transactionId;
    private String status;
    private boolean success;
}
