package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.proxies.CheckoutRequestDto;
import dev.eshan.productservice.dtos.proxies.CheckoutResponseDto;
import dev.eshan.productservice.dtos.proxies.PaymentResponseDto;
import dev.eshan.productservice.services.commons.OkHttpClientService;
import dev.eshan.productservice.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;

import static dev.eshan.productservice.utils.Utils.APPLICATION_JSON;

@Service
public class CheckoutServiceProxyImpl {
    @Value("${order.service.base.url}")
    private String orderServiceBaseUrl;

    private final OkHttpClientService okHttpClientService;

    public CheckoutServiceProxyImpl(OkHttpClientService okHttpClientService) {
        this.okHttpClientService = okHttpClientService;
    }

    public PaymentResponseDto checkout(String userId, CheckoutRequestDto checkoutRequest) throws IOException {
        // First need to call - to get the payment url
        CheckoutResponseDto checkoutResponse = null;
        try {
            String checkoutUrl = orderServiceBaseUrl + "/api/v1/checkout?userId=" + userId;
            String response = okHttpClientService.postCall(checkoutUrl, RequestBody.create(MediaType.parse(APPLICATION_JSON), Utils.gson.toJson(checkoutRequest)), new HashMap<>());
            checkoutResponse = Utils.gson.fromJson(response, CheckoutResponseDto.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in creating order, please try again later.");
        }

        // Secondly need to call - to get the gateway url
        PaymentResponseDto paymentResponse = null;
        try {
            String paymentUrl = orderServiceBaseUrl + "/api/v1/payment/process?orderId=" + checkoutResponse.getOrderId();
            String response = okHttpClientService.postCall(paymentUrl, RequestBody.create(MediaType.parse(APPLICATION_JSON), ""), new HashMap<>());
            paymentResponse = Utils.gson.fromJson(response, PaymentResponseDto.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in processing payment, please try again later.");
        }

        return paymentResponse;
    }

    public String getCheckoutStatus(String orderId, String userId) throws IOException {
        String statusUrl = orderServiceBaseUrl + "/api/v1/checkout/status?orderId=" + orderId + "&userId=" + userId;
        return okHttpClientService.getCall(statusUrl, "",new HashMap<>());
    }
}
