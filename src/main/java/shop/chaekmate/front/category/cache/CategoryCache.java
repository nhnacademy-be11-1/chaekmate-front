package shop.chaekmate.front.category.cache;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.category.service.CategoryService;

@Component
@RequiredArgsConstructor
public class CategoryCache {

    // 카테고리 캐싱
    private final CategoryService categoryService;
    private List<CategoryResponse> categories;

    @PostConstruct
    public void loadCategories() {
        this.categories = categoryService.getCategories();
    }

    public List<CategoryResponse> getCachedCategories() {
        return categories;
    }

    public void reload() { // 필요시 수동 리로드 가능
        this.categories = categoryService.getCategories();
    }
}
