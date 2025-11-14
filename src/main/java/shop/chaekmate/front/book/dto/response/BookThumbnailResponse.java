package shop.chaekmate.front.book.dto.response;

public record BookThumbnailResponse(
        Long bookImageId,
        String imageUrl,
        Boolean isThumbnail
) {
}
