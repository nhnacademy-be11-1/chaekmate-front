package shop.chaekmate.front.dto;

import java.time.LocalDateTime;
import java.util.List;

public record Book(
        Long id,
        String title,
        String index,
        String description,
        String author,
        String publisher,
        LocalDateTime publishedAt,
        String isbn,
        Integer price,
        Integer salesPrice,
        String imageUrl,
        Boolean isWrappable,
        Boolean isSaleEnd,
        Integer stock,
        Long views,
        List<Long> categoryIds,
        List<Long> tagIds) {}
