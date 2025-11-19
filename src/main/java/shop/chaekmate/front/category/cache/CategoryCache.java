package shop.chaekmate.front.category.cache;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import shop.chaekmate.front.category.dto.response.CategoryResponse;

@Component
public class CategoryCache {
    // 호출 실패 대비 저장용 캐시
    private List<CategoryResponse> categories = Collections.emptyList();

    public List<CategoryResponse> getCachedCategories() {
        return categories;
    }

    public void reload(List<CategoryResponse> newCategories) {
        this.categories = newCategories;
    }
}
