package dev.eshan.productservice.dtos.proxies;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutResponseDto {
    String url;
    String orderId;
}
