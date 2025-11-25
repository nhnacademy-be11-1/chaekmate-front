package shop.chaekmate.front.payment.dto.response;

import java.time.LocalDateTime;

public record PaymentAbortedResponse(
        String code,
        String message,
        LocalDateTime abortedAt
) {
}