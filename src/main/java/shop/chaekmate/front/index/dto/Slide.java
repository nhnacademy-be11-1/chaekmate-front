package shop.chaekmate.front.index.dto;

import lombok.Builder;
import lombok.Data;
import shop.chaekmate.front.book.dto.response.BookQueryResponse;

@Data
@Builder
public class Slide {
    //slide : subtitle, title, description, imageUrl, bookId
    String subtitle;
    String title;
    String description;
    String imageUrl;
    Long bookId;

    public static Slide of(String subtitle,String description, BookQueryResponse bookQueryResponse){
        return Slide.builder()
                .subtitle(subtitle)
                .title(bookQueryResponse.getTitle())
                .description(description)
                .imageUrl(bookQueryResponse.getThumbnailUrl())
                .bookId(bookQueryResponse.getId())
                .build();
    }
}
