package dev.eshan.productservice.dtos.proxies;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentStatusDto {
    private String transactionId;
    private String paymentStatus;
}
