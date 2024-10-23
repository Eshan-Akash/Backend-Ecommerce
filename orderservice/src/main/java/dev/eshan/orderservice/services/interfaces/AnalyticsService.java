package dev.eshan.orderservice.services.interfaces;

import dev.eshan.orderservice.dtos.OrderReportDto;
import dev.eshan.orderservice.dtos.OrderStatsDto;
import dev.eshan.orderservice.dtos.PaymentReportDto;

public interface AnalyticsService {

    OrderReportDto getOrderAnalytics(String startDate, String endDate);

    PaymentReportDto getPaymentAnalytics(String startDate, String endDate);

    OrderStatsDto getOrderStats();
}
