package shop.chaekmate.front.review.dto.request;

import java.util.List;

public record ReviewCreateRequest(
        Long memberId,
        Long orderedBookId,
        String comment,
        Integer rating,
        List<String> imageUrls
) {
}
