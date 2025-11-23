package shop.chaekmate.front.order.dto.response;

public record OrderSaveResponse(
        String orderNumber,
        Integer totalPrice
) {}
