package shop.chaekmate.front.payment.dto.request;

import lombok.Builder;
@Builder
public record PaymentCallbackRequest(
        String paymentKey,   // 성공 시 존재
        String orderId,      // 성공 시 존재
        String amount,       // 성공 시 존재
        String pointUsed,
        String code,         // 실패 시 존재
        String message       // 실패 시 존재
) {}
