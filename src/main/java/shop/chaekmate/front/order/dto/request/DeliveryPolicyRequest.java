package shop.chaekmate.front.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DeliveryPolicyRequest(

        @NotNull(message = "무료 배송 기준 금액은 필수입니다.")
        @Min(value = 0, message = "무료 배송 기준 금액은 0 이상이어야 합니다.")
        Integer freeStandardAmount,

        @NotNull(message = "배송비는 필수입니다.")
        @Min(value = 0, message = "배송비는 0 이상이어야 합니다.")
        Integer deliveryFee

) {}
