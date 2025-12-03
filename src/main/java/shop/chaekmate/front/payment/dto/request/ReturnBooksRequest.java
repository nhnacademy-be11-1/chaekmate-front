package shop.chaekmate.front.payment.dto.request;

import java.util.List;

public record ReturnBooksRequest(
        String orderNumber,
        String returnReason,
        List<CanceledBooksRequest> returnBooks
) { }