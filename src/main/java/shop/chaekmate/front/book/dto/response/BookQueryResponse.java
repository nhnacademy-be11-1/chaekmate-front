package shop.chaekmate.front.book.dto.response;

import lombok.Data;

@Data
public class BookQueryResponse {
    Long id;
    String title;
    String author;
    Integer price;
    Integer salesPrice;
    Double rating;
    Long reviewCount;
    String thumbnailUrl;
    Long views;
}
