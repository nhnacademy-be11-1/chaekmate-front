package shop.chaekmate.front.cart.dto.request;

public record CartItemCreateRequest(
        Long bookId,
        int quantity
) {
}
