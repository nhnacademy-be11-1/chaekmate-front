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

    public static BookModifyRequest of(AdminBookDetail adminBookDetail){

        return BookModifyRequest.builder()
                .title(adminBookDetail.title())
                .index(adminBookDetail.index())
                .description(adminBookDetail.description())
                .author(adminBookDetail.author())
                .publisher(adminBookDetail.publisher())
                .publishedAt(adminBookDetail.publishedAt())
                .isbn(adminBookDetail.isbn())
                .price(adminBookDetail.price())
                .salesPrice(adminBookDetail.salesPrice())
                .imageUrl(adminBookDetail.imageUrl())
                .isWrappable(adminBookDetail.isWrappable())
                .isSaleEnd(adminBookDetail.isSaleEnd())
                .stock(adminBookDetail.stock())
                .categoryIds(adminBookDetail.categoryIds())
                .tagIds(adminBookDetail.tagIds())
                .build();
    }

}
