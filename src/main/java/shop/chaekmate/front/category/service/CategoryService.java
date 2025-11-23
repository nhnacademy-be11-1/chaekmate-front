package shop.chaekmate.front.category.service;

import feign.FeignException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
                           CategoryCache categoryCache) {
        this.categoryAdaptor = categoryAdaptor;
        this.categoryCache = categoryCache;
    }

    public List<CategoryResponse> getCategories() {
        try {
            CommonResponse<List<CategoryResponse>> wrappedResponse = categoryAdaptor.getAllCategories();
            List<CategoryResponse> categories = wrappedResponse.data();

            // 호출 성공하면 캐시에 최신값 저장
            categoryCache.reload(categories);

            return categories;
        } catch (FeignException e) {
            // 호출 실패 시 캐시값 반환 (캐시가 비어있으면 빈 리스트 반환)
            List<CategoryResponse> cached = categoryCache.getCachedCategories();
            return cached != null ? cached : Collections.emptyList();
        }
    }

    public CategoryPageResponse<CategoryHierarchyResponse> getPagedCategories(int page, int size) {

        CommonResponse<CategoryPageResponse<CategoryHierarchyResponse>> wrappedResponse = categoryAdaptor.getPagedCategories(page, size);

        return wrappedResponse.data();
    }

    public void deleteCategoryById(Long id){

        categoryAdaptor.deleteCategory(id);
    }

    public void createCategory(Long parentCategoryId, String name){

        CategoryCreateRequest request = new CategoryCreateRequest(parentCategoryId, name);
        categoryAdaptor.createCategory(request);
    }

    public List<String> findNamesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<CategoryResponse> cachedCategories = categoryCache.getCachedCategories();

        // Create a map of ID -> Name for all categories by flattening the hierarchy
        Map<Long, String> allCategoriesMap = flattenCategories(cachedCategories)
                .collect(Collectors.toMap(CategoryResponse::id, CategoryResponse::name, (a, b) -> a)); // handle duplicates if any

        return ids.stream()
                .map(allCategoriesMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    // findNamesByIds 의 헬퍼 메소드 (계층 구조인 카테고리 평탄화)
    private Stream<CategoryResponse> flattenCategories(List<CategoryResponse> categories) {
        if (categories == null || categories.isEmpty()) {
            return Stream.empty();
        }
        return categories.stream()
                .flatMap(category -> Stream.concat(
                        Stream.of(category),
                        flattenCategories(category.children())
                ));
    }

}
