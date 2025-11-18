package shop.chaekmate.front.cart.dto.response;

public record CartItemAdvancedResponse(
        Long bookId,
        String bookImageUrl,
        String bookTitle,
        int bookSalesPrice,
        int quantity
) {
}
