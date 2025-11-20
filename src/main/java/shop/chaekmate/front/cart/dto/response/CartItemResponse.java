package shop.chaekmate.front.cart.dto.response;

public record CartItemResponse(
        Long bookId,
        int quantity
) {
}
