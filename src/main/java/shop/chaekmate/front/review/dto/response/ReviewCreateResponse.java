package shop.chaekmate.front.review.dto.response;

import lombok.Data;

@Data
public class ReviewCreateResponse {
    private Long id;
    private Long memberId;
    private Long orderedBookId;
    private String comment;
    private Integer rating;
}
