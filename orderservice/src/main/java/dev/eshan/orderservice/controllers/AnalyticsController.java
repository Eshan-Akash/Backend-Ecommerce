package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.OrderReportDto;
import dev.eshan.orderservice.dtos.OrderStatsDto;
import dev.eshan.orderservice.dtos.PaymentReportDto;
import dev.eshan.orderservice.services.interfaces.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/orders")
    public OrderReportDto getOrderAnalytics(@RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate) {
        return analyticsService.getOrderAnalytics(startDate, endDate);
    }

    @GetMapping("/payments")
    public PaymentReportDto getPaymentAnalytics(@RequestParam(required = false) String startDate,
                                                                @RequestParam(required = false) String endDate) {
        return analyticsService.getPaymentAnalytics(startDate, endDate);
    }

    @GetMapping("/order-stats")
    public OrderStatsDto getOrderStats() {
        return analyticsService.getOrderStats();
    }
}
