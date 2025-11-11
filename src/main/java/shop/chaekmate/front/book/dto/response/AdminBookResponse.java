package shop.chaekmate.front.book.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AdminBookResponse(
        Long id,
        String title,
        String index,
        String description,
        String author,
        String publisher,
        LocalDateTime publishedAt,
        String isbn,
        Long price,
        Long salesPrice,
        String imageUrl,
        boolean isWrappable,
        boolean isSaleEnd,
        Long stock,
        Long views,
        List<Long> categoryIds,
        List<Long> tagIds
) {
}
