package shop.chaekmate.front.payment.dto.request;

public record CanceledBooksRequest(
        Long orderedBookId,
        Integer canceledQuantity
) {}
