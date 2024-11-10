package dev.eshan.orderservice.controllers;

import dev.eshan.orderservice.dtos.OrderReportDto;
import dev.eshan.orderservice.dtos.OrderStatsDto;
import dev.eshan.orderservice.dtos.PaymentReportDto;
import dev.eshan.orderservice.services.interfaces.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/analytics")
@Slf4j
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/orders")
    public OrderReportDto getOrderAnalytics(@RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate) {
        try {
            return analyticsService.getOrderAnalytics(startDate, endDate);
        } catch (Exception e) {
            log.error("Error fetching order analytics", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching order analytics");
        }
    }

    @GetMapping("/payments")
    public PaymentReportDto getPaymentAnalytics(@RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate) {
        try {
            return analyticsService.getPaymentAnalytics(startDate, endDate);
        } catch (Exception e) {
            log.error("Error fetching payment analytics", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching payment analytics");
        }
    }

    @GetMapping("/order-stats")
    public OrderStatsDto getOrderStats() {
        try {
            return analyticsService.getOrderStats();
        } catch (Exception e) {
            log.error("Error fetching order stats", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching order stats");
        }
    }
}
