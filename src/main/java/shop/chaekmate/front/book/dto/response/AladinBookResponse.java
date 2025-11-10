package shop.chaekmate.front.book.dto.response;

public record AladinBookResponse(
        String title,
        String author,
        String publisher,
        String publishedAt,
        String isbn,
        Long price,
        Long salesPrice,
        String coverImage,
        String description,
        String aladinCategoryName
) {
}
