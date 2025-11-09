package shop.chaekmate.front.payment.dto.response;

import java.time.OffsetDateTime;

public record PaymentApproveResponse(

        String orderId,

//        String paymentKey,

        long totalAmount,

        String status,

        OffsetDateTime approvedAt
) {}
