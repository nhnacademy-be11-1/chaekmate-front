package shop.chaekmate.front.book.dto.response;

import lombok.Builder;
import shop.chaekmate.front.category.service.CategoryService;
import shop.chaekmate.front.tag.service.TagService;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BookDetail(
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
        List<String> categoryNames,
        List<String> tagNames
) {

    public static BookDetail of(AdminBookResponse book, CategoryService categoryService, TagService tagService) {
        List<String> categoryNames = categoryService.findNamesByIds(book.categoryIds());
        List<String> tagNames = tagService.findNamesByIds(book.tagIds());

        return BookDetail.builder()
                .id(book.id())
                .title(book.title())
                .index(book.index())
                .description(book.description())
                .author(book.author())
                .publisher(book.publisher())
                .publishedAt(book.publishedAt())
                .isbn(book.isbn())
                .price(book.price())
                .salesPrice(book.salesPrice())
                .imageUrl(book.imageUrl())
                .isWrappable(book.isWrappable())
                .isSaleEnd(book.isSaleEnd())
                .stock(book.stock())
                .views(book.views())
                .categoryNames(categoryNames)
                .tagNames(tagNames)
                .build();
    }
}
