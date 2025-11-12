package shop.chaekmate.front.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import shop.chaekmate.front.book.dto.response.AdminBookDetail;

@Builder
public record BookModifyRequest(
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
        List<Long> categoryIds,
        List<Long> tagIds
) {

    public static BookModifyRequest of(BookModificationRequest modificationRequest){

        return BookModifyRequest.builder()
                .title(modificationRequest.title())
                .index(modificationRequest.index())
                .description(modificationRequest.description())
                .author(modificationRequest.author())
                .publisher(modificationRequest.publisher())
                .publishedAt(modificationRequest.publishedAt())
                .isbn(modificationRequest.isbn())
                .price(modificationRequest.price())
                .salesPrice(modificationRequest.salesPrice())
                .imageUrl(modificationRequest.newThumbnailUrl())
                .isWrappable(modificationRequest.isWrappable())
                .isSaleEnd(modificationRequest.isSaleEnd())
                .stock(modificationRequest.stock())
                .categoryIds(modificationRequest.categoryIds())
                .tagIds(modificationRequest.tagIds())
                .build();
    }

}
