package shop.chaekmate.front.book.dto.request;

public record LikeRequest(
        Long bookId,
        String actionType
) {
}
