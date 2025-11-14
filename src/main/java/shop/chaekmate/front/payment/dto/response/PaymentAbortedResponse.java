package shop.chaekmate.front.payment.dto.response;

import java.time.OffsetDateTime;

public record PaymentAbortedResponse(
        String code,
        String message,
        OffsetDateTime occurredAt
) {
}
