package shop.chaekmate.front.book.dto.response;

public record BookImageResponse(
        Long bookImageId,
        String imageUrl,
        Boolean isThumbnail
) {
}
