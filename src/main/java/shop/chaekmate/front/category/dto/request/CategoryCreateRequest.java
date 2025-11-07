package shop.chaekmate.front.category.dto.request;

import jakarta.validation.constraints.Size;

public record CategoryCreateRequest (
        Long parentCategoryId,
        @Size(max = 255, message = "카테고리명은 255자 이하여야 합니다.")
        String name) { }
