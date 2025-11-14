package shop.chaekmate.front.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record BookModificationRequest(
    // Book details
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
    Long stock,
    Boolean isWrappable,
    Boolean isSaleEnd,
    Long views,
    List<Long> categoryIds,
    List<Long> tagIds,

    // Image management
    String newThumbnailUrl,
    List<String> newDetailImageUrls,
    List<Long> deletedImageIds
) {}
