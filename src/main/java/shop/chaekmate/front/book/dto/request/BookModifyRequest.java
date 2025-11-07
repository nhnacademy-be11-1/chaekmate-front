package shop.chaekmate.front.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import shop.chaekmate.front.book.dto.response.BookDetail;

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

    public static BookModifyRequest of(BookDetail bookDetail){

        return BookModifyRequest.builder()
                .title(bookDetail.title())
                .index(bookDetail.index())
                .description(bookDetail.description())
                .author(bookDetail.author())
                .publisher(bookDetail.publisher())
                .publishedAt(bookDetail.publishedAt())
                .isbn(bookDetail.isbn())
                .price(bookDetail.price())
                .salesPrice(bookDetail.salesPrice())
                .imageUrl(bookDetail.imageUrl())
                .isWrappable(bookDetail.isWrappable())
                .isSaleEnd(bookDetail.isSaleEnd())
                .stock(bookDetail.stock())
                .categoryIds(bookDetail.categoryIds())
                .tagIds(bookDetail.tagIds())
                .build();
    }

}
