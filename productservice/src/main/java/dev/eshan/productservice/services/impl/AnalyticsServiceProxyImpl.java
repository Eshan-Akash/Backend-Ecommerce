package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.proxies.OrderReportDto;
import dev.eshan.productservice.dtos.proxies.OrderStatsDto;
import dev.eshan.productservice.dtos.proxies.PaymentReportDto;
import dev.eshan.productservice.services.commons.OkHttpClientService;
import dev.eshan.productservice.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class AnalyticsServiceProxyImpl {
    private final OkHttpClientService okHttpClientService;
    private final String analyticsServiceBaseUrl;

    public AnalyticsServiceProxyImpl(OkHttpClientService okHttpClientService, @Value("${order.service.base.url}") String analyticsServiceBaseUrl) {
        this.okHttpClientService = okHttpClientService;
        this.analyticsServiceBaseUrl = analyticsServiceBaseUrl;
    }

    public OrderReportDto getOrderAnalytics(String startDate, String endDate) throws IOException {
        String url = analyticsServiceBaseUrl + "/api/v1/analytics/orders?startDate=" + startDate + "&endDate=" + endDate;
        String response = okHttpClientService.getCall(url, "", new HashMap<>());
        return Utils.gson.fromJson(response, OrderReportDto.class);
    }

    public PaymentReportDto getPaymentAnalytics(String startDate, String endDate) throws IOException {
        String url = analyticsServiceBaseUrl + "/api/v1/analytics/payments?startDate=" + startDate + "&endDate=" + endDate;
        String response = okHttpClientService.getCall(url, "", new HashMap<>());
        return Utils.gson.fromJson(response, PaymentReportDto.class);
    }

    public OrderStatsDto getOrderStats() throws IOException {
        String url = analyticsServiceBaseUrl + "/api/v1/analytics/order-stats";
        String response = okHttpClientService.getCall(url, "", new HashMap<>());
        return Utils.gson.fromJson(response, OrderStatsDto.class);
    }
}
