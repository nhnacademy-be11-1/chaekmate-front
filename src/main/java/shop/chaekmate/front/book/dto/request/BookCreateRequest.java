package shop.chaekmate.front.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {
    private String title;
    private String author;
    private Integer price;
    private Integer stock;
    private String description;
    private String publishedDate;
    private String isbn;
    private List<Long> categoryIds;
    private String thumbnailUrl;
    private List<String> detailImageUrls;
}
