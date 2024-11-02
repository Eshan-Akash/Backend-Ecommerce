package dev.eshan.productservice.dtos.proxies;

import lombok.Data;

@Data
public class RetryPaymentDto {
    private String orderId;
}
