package shop.chaekmate.front.order.dto.response;

public record DeliveryPolicyResponse(
    int freeStandardAmount,
    int deliveryFee
) {}