package shop.chaekmate.front.index.dto;

import lombok.Builder;
import lombok.Data;
import shop.chaekmate.front.book.dto.response.BookQueryResponse;

@Data
@Builder
public class BookSliceInfo {
    // id, title, author, price, salesPrice, rating, reviewCount, imageUrl
    Long id;
    String title;
    String author;
    Integer price;
    Integer salesPrice;
    Double rating;
    Long reviewCount;
    String imageUrl;
    Long views;

    public static BookSliceInfo of(BookQueryResponse bookQueryResponse){
        return BookSliceInfo
                .builder()
                .id(bookQueryResponse.getId())
                .title(bookQueryResponse.getTitle())
                .author(bookQueryResponse.getAuthor())
                .price(bookQueryResponse.getPrice())
                .salesPrice(bookQueryResponse.getSalesPrice())
                .rating(bookQueryResponse.getRating())
                .reviewCount(bookQueryResponse.getReviewCount())
                .imageUrl(bookQueryResponse.getThumbnailUrl())
                .views(bookQueryResponse.getViews())
                .build();
    }
}
