package shop.chaekmate.front.book.dto;

public record BookDetailResponse(
        Long id,
        String title,
        String index,
        String description,
        String author,
        String publisher,
        Integer price,
        Integer salesPrice,
        String imageUrl,
        Integer stock,
        Long views
) {
}
