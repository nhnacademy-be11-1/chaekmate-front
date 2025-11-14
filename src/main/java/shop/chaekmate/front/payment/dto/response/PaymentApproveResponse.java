package shop.chaekmate.front.payment.dto.response;

import java.time.OffsetDateTime;

public record PaymentApproveResponse(

        String orderId,

        long totalAmount,

        int pointUsed,

        String status,

        OffsetDateTime approvedAt
) {}
