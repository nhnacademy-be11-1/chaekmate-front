package shop.chaekmate.front.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CartItemUpdateRequest(
        int quantity
) {
}
