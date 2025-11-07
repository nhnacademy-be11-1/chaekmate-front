package shop.chaekmate.front.category.service;

import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.category.adaptor.CategoryAdaptor;
import shop.chaekmate.front.category.cache.CategoryCache;
import shop.chaekmate.front.category.dto.request.CategoryCreateRequest;
import shop.chaekmate.front.category.dto.response.CategoryHierarchyResponse;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.category.dto.response.CategoryPageResponse;

@Service
public class CategoryService {

    private final CategoryAdaptor categoryAdaptor;
    private final CategoryCache categoryCache;

    public CategoryService(CategoryAdaptor categoryAdaptor,
                           @Lazy CategoryCache categoryCache) { // 순환 의존성 해결
        this.categoryAdaptor = categoryAdaptor;
        this.categoryCache = categoryCache;
    }

    public List<CategoryResponse> getCategories() {

        CommonResponse<List<CategoryResponse>> wrappedResponse = categoryAdaptor.getAllCategories();

        return wrappedResponse.data();
    }

    public CategoryPageResponse<CategoryHierarchyResponse> getPagedCategories(int page, int size) {

        CommonResponse<CategoryPageResponse<CategoryHierarchyResponse>> wrappedResponse = categoryAdaptor.getPagedCategories(page, size);

        return wrappedResponse.data();
    }

    public void deleteCategoryById(Long id){

        categoryAdaptor.deleteCategory(id);
        categoryCache.reload(); // 캐시 리로드
    }

    public void createCategory(Long parentCategoryId, String name){

        CategoryCreateRequest request = new CategoryCreateRequest(parentCategoryId, name);
        categoryAdaptor.createCategory(request);
        categoryCache.reload(); // 캐시 리로드

    }

    // 카테고리 Id 들의 이름 찾아주는 메소드
    public List<String> findNamesByIds(List<Long> ids){

        // 캐시된걸 사용
        List<CategoryResponse> cachedCategories = categoryCache.getCachedCategories();

        return cachedCategories.stream().filter(c -> ids.contains(c.id())).map(CategoryResponse::name).toList();
    }
}
