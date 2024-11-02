package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.proxies.PaymentConfirmationResponse;
import dev.eshan.productservice.dtos.proxies.PaymentResponseDto;
import dev.eshan.productservice.dtos.proxies.PaymentStatusDto;
import dev.eshan.productservice.dtos.proxies.RetryPaymentDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.commons.OkHttpClientService;
import dev.eshan.productservice.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

import static dev.eshan.productservice.utils.Utils.APPLICATION_JSON;

@Service
public class PaymentServiceProxyImpl {
    @Value("${order.service.base.url}")
    private String orderServiceBaseUrl;
    private final OkHttpClientService okHttpClientService;

    public PaymentServiceProxyImpl(OkHttpClientService okHttpClientService) {
        this.okHttpClientService = okHttpClientService;
    }

    public PaymentConfirmationResponse confirmPayment(String orderId, String userId) throws IOException {
        // URL to confirm payment
        String confirmPaymentUrl = orderServiceBaseUrl + "/api/v1/payment/confirm?orderId=" + orderId + "&userId=" + userId;
        String response = okHttpClientService.postCall(confirmPaymentUrl,
                RequestBody.create(MediaType.parse(APPLICATION_JSON), ""), new HashMap<>());
        return Utils.gson.fromJson(response, PaymentConfirmationResponse.class);
    }

    public PaymentStatusDto getPaymentStatus(String paymentId, String userId) throws IOException {
        String paymentStatusUrl = orderServiceBaseUrl + "/api/v1/payment/status/" + paymentId + "?userId=" + userId;
        String response = okHttpClientService.getCall(paymentStatusUrl, "", new HashMap<>());
        return Utils.gson.fromJson(response, PaymentStatusDto.class);
    }

    public PaymentResponseDto retryPayment(RetryPaymentDto retryPayment, String userId) throws IOException {
        String retryPaymentUrl = orderServiceBaseUrl + "/api/v1/payment/retry" + "?userId=" + userId;
        String response = okHttpClientService.postCall(retryPaymentUrl,
                RequestBody.create(MediaType.parse(APPLICATION_JSON), Utils.gson.toJson(retryPayment)), new HashMap<>());
        return Utils.gson.fromJson(response, PaymentResponseDto.class);
    }
}