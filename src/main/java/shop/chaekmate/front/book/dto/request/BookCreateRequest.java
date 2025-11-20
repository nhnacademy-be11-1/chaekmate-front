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
                .title(creationRequest.getTitle())
                .index(creationRequest.getIndex())
                .description(creationRequest.getDescription())
                .author(creationRequest.getAuthor())
                .publisher(creationRequest.getPublisher())
                .publishedAt(creationRequest.getPublishedAt())
                .isbn(creationRequest.getIsbn())
                .price(creationRequest.getPrice())
                .salesPrice(creationRequest.getSalesPrice())
                .isWrappable(creationRequest.getIsWrappable())
                .isSaleEnd(creationRequest.getIsSaleEnd())
                .stock(creationRequest.getStock())
                .categoryIds(creationRequest.getCategoryIds())
                .tagIds(creationRequest.getTagIds())
                .build();
    }
}
