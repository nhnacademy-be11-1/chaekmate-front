package shop.chaekmate.front.tag.dto.response;

import java.util.List;

public record TagPageResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
