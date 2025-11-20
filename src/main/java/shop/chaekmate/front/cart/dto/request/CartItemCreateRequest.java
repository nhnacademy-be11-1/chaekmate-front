package shop.chaekmate.front.cart.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CartItemCreateRequest(
        Long bookId,
        int quantity
) {
}
