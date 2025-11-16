package shop.chaekmate.front.payment.dto.request;


public record PaymentReadyRequest(
    String orderNumber,
    String orderName,
    long price,
    Integer pointUsed
) {}
