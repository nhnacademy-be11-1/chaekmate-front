package shop.chaekmate.front.book.dto.response;

public record AdminBookSearchResponse(
        Long id,
        String title,
        String author,
        String publisher,
        int price,
        int salesPrice,
        String imageUrl
) {
}
