package shop.chaekmate.front.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record BookCreationRequest(
        String title,
        String index,
        String description,
        String author,
        String publisher,
        LocalDateTime publishedAt,
        String isbn,
        Long price,
        Long salesPrice,
        String thumbnailUrl,
        List<String> newDetailImageUrls,
        Boolean isWrappable,
        Boolean isSaleEnd,
        Long stock,
        List<Long> categoryIds,
        List<Long> tagIds
) {
}
