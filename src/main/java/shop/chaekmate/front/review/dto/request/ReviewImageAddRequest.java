package shop.chaekmate.front.review.dto.request;

import java.util.List;

public record ReviewImageAddRequest(
        List<String> imageUrls
) {
}
