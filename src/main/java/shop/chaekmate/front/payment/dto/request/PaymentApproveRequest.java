package shop.chaekmate.front.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentApproveRequest(

        @NotBlank(message = "결제 수단 선택은 필수 값입니다.")
        String paymentType,

        @NotBlank(message = "결제 키는 필수 값입니다.")
        String paymentKey,

        @NotBlank(message = "주문 번호는 필수 입력 값입니다.")
        String orderId,

        @Positive(message = "승인 금액은 0보다 커야 합니다.")
        long amount
) {}
