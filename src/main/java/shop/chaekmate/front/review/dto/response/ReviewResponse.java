package shop.chaekmate.front.review.dto.response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long memberId,
        Long orderedBookId,
        String comment,
        Integer rating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

