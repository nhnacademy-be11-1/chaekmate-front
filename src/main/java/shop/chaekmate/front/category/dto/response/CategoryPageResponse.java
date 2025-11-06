package shop.chaekmate.front.category.dto.response;

import java.util.List;

public record CategoryPageResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
