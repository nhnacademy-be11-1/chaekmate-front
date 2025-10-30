package shop.chaekmate.front.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record Book(
        @JsonProperty("bookId") Long id,
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
        Boolean isWrappeable,
        Boolean isSaleEnd,
        Integer stock,
        Long views,
        List<Long> categoryIds,
        List<Long> tagIds) {}
