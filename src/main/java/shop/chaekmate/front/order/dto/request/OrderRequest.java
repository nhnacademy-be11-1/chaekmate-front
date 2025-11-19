package shop.chaekmate.front.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
        @NotNull(message = "책 ID 필수값입니다.")
        Long bookId,
        @Positive(message = "책 수량은 음수 또는 0이 될 수 없습니다.")
        Integer quantity
) {}

