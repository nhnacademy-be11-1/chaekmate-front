package shop.chaekmate.front.book.dto.response;

public record LikeResponse(
        Long id,
        Long bookId,
        Long memberId
) {
}
