package shop.chaekmate.front.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import shop.chaekmate.front.payment.dto.request.PaymentCancelRequest;

public record PaymentCancelResponse(

        @JsonProperty("orderId")
        String orderNumber,

        String cancelReason,

        long canceledCash,

        int canceledPoint,

        LocalDateTime canceledAt,

        List<PaymentCancelRequest> canceledBooks
) {}

