package shop.chaekmate.front.order.dto.response;

import java.time.LocalDateTime;

public record DeliveryPolicyHistoryResponse(

        Integer freeStandardAmount,
        Integer deliveryFee,
        LocalDateTime createdAt,
        LocalDateTime deletedAt

) {}
