package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.OrderReportDto;
import dev.eshan.orderservice.dtos.OrderStatsDto;
import dev.eshan.orderservice.dtos.PaymentReportDto;
import dev.eshan.orderservice.services.interfaces.AnalyticsService;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    @Override
    public OrderReportDto getOrderAnalytics(String startDate, String endDate) {
        return null;
    }

    @Override
    public PaymentReportDto getPaymentAnalytics(String startDate, String endDate) {
        return null;
    }

    @Override
    public OrderStatsDto getOrderStats() {
        return null;
    }
}
