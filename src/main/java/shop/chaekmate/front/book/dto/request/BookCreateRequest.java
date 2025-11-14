package shop.chaekmate.front.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record BookCreateRequest (
    String title,
    String index,
    String description,
    String author,
    String publisher,
    LocalDateTime publishedAt,
    String isbn,
    Long price,
    Long salesPrice,
    Boolean isWrappable,
    Boolean isSaleEnd,
    Long stock,
    List<Long> categoryIds,
    List<Long> tagIds
){

    public static BookCreateRequest of(BookCreationRequest creationRequest){

        return BookCreateRequest.builder()
                .title(creationRequest.title())
                .index(creationRequest.index())
                .description(creationRequest.description())
                .author(creationRequest.author())
                .publisher(creationRequest.publisher())
                .publishedAt(creationRequest.publishedAt())
                .isbn(creationRequest.isbn())
                .price(creationRequest.price())
                .salesPrice(creationRequest.salesPrice())
                .isWrappable(creationRequest.isWrappable())
                .isSaleEnd(creationRequest.isSaleEnd())
                .stock(creationRequest.stock())
                .categoryIds(creationRequest.categoryIds())
                .tagIds(creationRequest.tagIds())
                .build();
    }
}
