package shop.chaekmate.front.category.dto.response;

import java.util.List;

public record CategoryResponse(Long id, String name, List<CategoryResponse> children) {
}
