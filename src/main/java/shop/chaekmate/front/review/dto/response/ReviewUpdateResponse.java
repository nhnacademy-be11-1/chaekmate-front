package shop.chaekmate.front.review.dto.response;

public record ReviewUpdateResponse(
        Long id,
        Long memberId,
        Long orderedBookId,
        String comment,
        Integer rating
) {
}

