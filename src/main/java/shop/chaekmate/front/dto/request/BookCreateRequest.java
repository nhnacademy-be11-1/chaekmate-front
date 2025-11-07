package shop.chaekmate.front.dto.request;

import java.time.LocalDate;
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
    private LocalDate publicationDate;
    private String isbn;
    private String description;
    private Integer price;
    private Integer stock;
}
