package shop.chaekmate.front.cart.dto.response;

import java.util.List;

public record CartItemListResponse(
        Long cartId,
        List<CartItemResponse> items
) {
}
