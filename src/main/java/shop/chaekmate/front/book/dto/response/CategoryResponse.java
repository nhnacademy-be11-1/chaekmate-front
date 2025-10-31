package shop.chaekmate.front.book.dto.response;

import java.util.List;

public record CategoryResponse(Long id, String name, List<CategoryResponse> children) { }
