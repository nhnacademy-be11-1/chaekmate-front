package shop.chaekmate.front.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PaymentCancelRequest(
        String paymentKey,
        @JsonProperty("orderId")
        String orderNumber,
        String cancelReason,
        long cancelAmount,
        List<CanceledBooksRequest> canceledBooks
) {}
