package dev.eshan.productservice.services.impl;

import com.google.common.reflect.TypeToken;
import dev.eshan.productservice.dtos.proxies.OrderDto;
import dev.eshan.productservice.dtos.proxies.TrackingStatusDto;
import dev.eshan.productservice.dtos.proxies.UserDetails;
import dev.eshan.productservice.services.commons.OkHttpClientService;
import dev.eshan.productservice.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.eshan.productservice.utils.Utils.APPLICATION_JSON;

@Service
public class OrderServiceProxyImpl {
    private final OkHttpClientService okHttpClientService;

    @Value("${order.service.base.url}")
    private String orderServiceBaseUrl;

    @Value("${order.service.token}")
    private String orderServiceToken;

    public OrderServiceProxyImpl(OkHttpClientService okHttpClientService) {
        this.okHttpClientService = okHttpClientService;
    }

    public OrderDto createOrder(String userId, UserDetails userDetails) throws IOException {
        String url = orderServiceBaseUrl + "/api/v1/order/create?userId=" + userId;
        String response = okHttpClientService.postCall(url,
                RequestBody.create(Utils.gson.toJson(userDetails), MediaType.get("application/json; charset=utf-8")
                ), getOrderServiceRequestHeaders());
        return Utils.gson.fromJson(response, OrderDto.class);
    }

    public OrderDto getOrderById(String orderId, String userId) throws IOException {
        String url = orderServiceBaseUrl + "/api/v1/order" + orderId + "?userId=" + userId;
        String response = okHttpClientService.getCall(url, "", getOrderServiceRequestHeaders());
        return Utils.gson.fromJson(response, OrderDto.class);
    }

    public List<OrderDto> getOrderHistory(String userId) throws IOException {
        String url = orderServiceBaseUrl + "/api/v1/order/history?userId=" + userId;
        String response = okHttpClientService.getCall(url, "", getOrderServiceRequestHeaders());
        return Utils.gsonSnakeCase.fromJson(response,
                new TypeToken<ArrayList<OrderDto>>() {
                }.getType());
    }

    public TrackingStatusDto trackOrder(String orderId, String userId) throws IOException {
        String url = orderServiceBaseUrl + "/api/v1/order/track/" + orderId + "?userId=" + userId;
        String response = okHttpClientService.getCall(url, "", getOrderServiceRequestHeaders());
        return Utils.gson.fromJson(response, TrackingStatusDto.class);
    }

    public Map<String, String> getOrderServiceRequestHeaders() {
        return Map.ofEntries(
                Map.entry("Content-Type", APPLICATION_JSON),
                Map.entry("Authorization", "Bearer " + orderServiceToken));
    }
}