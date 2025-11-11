package shop.chaekmate.front.payment.dto.request;

import lombok.Builder;

@Builder
public record PaymentReadyRequest(
    String orderNumber,
    String orderName,
    long price
) {}
