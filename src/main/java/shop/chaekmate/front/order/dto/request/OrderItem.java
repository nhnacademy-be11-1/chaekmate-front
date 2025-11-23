package shop.chaekmate.front.order.dto.request;

public record OrderItem(
        Long bookId,
        String title,
        String author,
        String publisher,
        int originalPrice,
        int salesPrice,
        int discountRate,
        int discountAmount,
        int quantity,
        int subtotal,
        String thumbnailUrl
) {}
