package shop.chaekmate.front.cart.dto.response;

public record CartItemUpdateResponse(
        Long bookId,
        int quantity
) {
}
