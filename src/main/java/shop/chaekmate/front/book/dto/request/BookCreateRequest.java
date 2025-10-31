package shop.chaekmate.front.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {
    private String title;
    private String author;
    private String publisher;
    private LocalDateTime publishedAt;
    private String isbn;
    private String description;
    private String index;
    private Integer price;
    private Integer salesPrice;
    private Integer stock;
    private Boolean isWrappable;
    private Boolean isSaleEnd;
    private String imageUrl;
    private List<Long> tagIds;
    private List<Long> categoryIds;
}
