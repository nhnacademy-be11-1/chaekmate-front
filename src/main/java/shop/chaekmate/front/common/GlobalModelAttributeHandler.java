package shop.chaekmate.front.common;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.category.service.CategoryService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeHandler {
    // 모든 페이지에서 카테고리들 주입

    private final CategoryService categoryService; // Service에서 캐시 관리

    @ModelAttribute("categories")
    public List<CategoryResponse> addCategoriesToModel() {
        // Service가 캐시 확인 후 API 호출까지 알아서 처리
        return categoryService.getCategories();
    }

}
