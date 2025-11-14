package shop.chaekmate.front.search.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultResponse {
    long id;
    String title;
    String author;
    Integer price;
    String description;
    String bookImages;
    List<String> categories;
    LocalDate publicationDatetime;
    List<String> tags;
    String reviewSummary;
    int reviewCnt;
    double rating;
    String publisher;
}
