package dev.eshan.orderservice.networks;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentRedirectResponse generatePaymentRedirectURL(String orderId, double amount) {
        // Mock URL generation
        String redirectUrl = "https://mockpaymentgateway.com/pay?orderId=" + orderId + "&amount=" + amount;
        String transactionId = UUID.randomUUID().toString();

        return new PaymentRedirectResponse(redirectUrl, transactionId);
    }

    @Override
    public PaymentVerificationResponse verifyPayment(String transactionId) {
        // Mock verification process
        boolean isSuccess = Math.random() < 0.8; // 80% chance of success, for instance
        String status = isSuccess ? "SUCCESS" : "FAILED";

        return new PaymentVerificationResponse(transactionId, status, isSuccess);
    }
}
