package shop.chaekmate.front.payment.dto.request;

public record PaymentCallbackRequest(
        String paymentKey,
        String orderId,
        Long amount,
        Integer pointUsed,
        String code,
        String message
) {}
