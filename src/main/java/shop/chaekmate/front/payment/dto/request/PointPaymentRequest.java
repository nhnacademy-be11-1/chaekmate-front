package shop.chaekmate.front.payment.dto.request;

public record PointPaymentRequest(
    String orderId,
    Integer pointUsed
){}
