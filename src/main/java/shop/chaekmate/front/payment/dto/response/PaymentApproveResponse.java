package shop.chaekmate.front.payment.dto.response;

import java.time.LocalDateTime;

public record PaymentApproveResponse(

        String orderId,

        long totalAmount,

        int pointUsed,

        String status,

        LocalDateTime approvedAt
) {}