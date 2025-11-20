package shop.chaekmate.front.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BookCreationRequest {
    private String title;
    private String index;
    private String description;
    private String author;
    private String publisher;
    private LocalDateTime publishedAt;
    private String isbn;
    private Long price;
    private Long salesPrice;
    private String thumbnailUrl;
    private List<String> newDetailImageUrls;
    private Boolean isWrappable;
    private Boolean isSaleEnd;
    private Long stock;
    private List<Long> categoryIds;
    private List<Long> tagIds;
    private String aladinCategoryName;
}
