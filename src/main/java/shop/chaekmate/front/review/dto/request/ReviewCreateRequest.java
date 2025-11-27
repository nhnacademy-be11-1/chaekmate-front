package shop.chaekmate.front.review.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class ReviewCreateRequest {
    private Long memberId;
    private Long orderedBookId;
    private String comment;
    private Integer rating;
    private List<String> imageUrls;
}
