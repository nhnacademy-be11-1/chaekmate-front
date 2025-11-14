package shop.chaekmate.front.point.dto.response;

import java.lang.reflect.Member;

public record PointHistoryResponse (
        Long id,
        String member,
        String type,
        int point,
        String source
){ }
