package shop.chaekmate.front.review.dto.request;

import java.util.List;

public record ReviewUpdateRequest(
        String comment,
        Integer rating,
        List<String> imageUrls
) {
}

