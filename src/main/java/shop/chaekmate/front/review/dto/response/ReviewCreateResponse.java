package shop.chaekmate.front.review.dto.response;

public record ReviewCreateResponse(
        Long id,
        Long memberId,
        Long orderedBookId,
        String comment,
        Integer rating
) {
}
