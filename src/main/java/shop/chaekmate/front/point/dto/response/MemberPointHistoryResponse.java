package shop.chaekmate.front.point.dto.response;

import java.time.LocalDateTime;

public record MemberPointHistoryResponse(
        Long member,
        String type,
        int point,
        String source,
        LocalDateTime createdAt
) {
}
