package shop.chaekmate.front.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record PaymentApproveRequest(

        @NotBlank(message = "결제 수단 선택은 필수 값입니다.")
        String paymentType,

        @NotBlank(message = "결제 키는 필수 값입니다.")
        String paymentKey,

        @NotBlank(message = "주문 번호는 필수 입력 값입니다.")
        String orderId,

        @PositiveOrZero(message = "승인 금액은 음수가 될 수 없습니다.")
        long amount
) {}
