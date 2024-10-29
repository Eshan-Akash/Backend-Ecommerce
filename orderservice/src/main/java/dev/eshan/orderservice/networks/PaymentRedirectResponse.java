package dev.eshan.orderservice.networks;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRedirectResponse {
    private String redirectUrl;
    private String transactionId;
}
