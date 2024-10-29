package dev.eshan.orderservice.networks;

public interface PaymentGateway {
    PaymentRedirectResponse generatePaymentRedirectURL(String orderId, double amount);
    PaymentVerificationResponse verifyPayment(String transactionId);
}
