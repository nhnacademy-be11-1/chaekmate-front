package shop.chaekmate.front.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest { // Category Update (PUT) 시에도 사용
    @Size(max = 255)
    private String name;
    private Long parentCategoryId;
}
