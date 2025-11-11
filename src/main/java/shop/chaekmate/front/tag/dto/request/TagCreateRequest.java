package shop.chaekmate.front.tag.dto.request;

import jakarta.validation.constraints.Size;

public record TagCreateRequest(
        @Size(max = 255, message = "태그명은 255자 이하여야 합니다.")
        String name) {
}
