package dev.eshan.productservice.controllers.proxies;

import dev.eshan.productservice.dtos.proxies.OrderReportDto;
import dev.eshan.productservice.dtos.proxies.OrderStatsDto;
import dev.eshan.productservice.dtos.proxies.PaymentReportDto;
import dev.eshan.productservice.services.impl.AnalyticsServiceProxyImpl;
import dev.eshan.productservice.utils.UserData;
import dev.eshan.productservice.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsServiceProxyImpl analyticsServiceProxyImpl;

    public AnalyticsController(AnalyticsServiceProxyImpl analyticsServiceProxyImpl) {
        this.analyticsServiceProxyImpl = analyticsServiceProxyImpl;
    }


    @GetMapping("/orders")
    public OrderReportDto getOrderAnalytics(@RequestParam(required = true) String startDate,
                                            @RequestParam(required = true) String endDate) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return analyticsServiceProxyImpl.getOrderAnalytics(startDate, endDate);
    }

    @GetMapping("/payments")
    public PaymentReportDto getPaymentAnalytics(@RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return analyticsServiceProxyImpl.getPaymentAnalytics(startDate, endDate);
    }

    @GetMapping("/order-stats")
    public OrderStatsDto getOrderStats() throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = Utils.createUserDataFromToken(jwt);
        if (!userData.getUserRole().contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return analyticsServiceProxyImpl.getOrderStats();
    }
}
