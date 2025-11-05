package shop.chaekmate.front.common;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.chaekmate.front.category.cache.CategoryCache;
import shop.chaekmate.front.category.dto.response.CategoryResponse;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeHandler {
    // 모든 페이지에서 카테고리들 주입

    private final CategoryCache categoryCache; // 내부적으로 캐시 사용 중

    @ModelAttribute("categories")
    public List<CategoryResponse> addCategoriesToModel() {
        return categoryCache.getCachedCategories();
    }
}
