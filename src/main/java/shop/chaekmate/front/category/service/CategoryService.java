package shop.chaekmate.front.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.category.adaptor.CategoryAdaptor;
import shop.chaekmate.front.category.dto.response.CategoryHierarchyResponse;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.category.dto.response.CategoryPageResponse;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryAdaptor categoryAdaptor;

    public List<CategoryResponse> getCategories() {

        CommonResponse<List<CategoryResponse>> wrappedResponse = categoryAdaptor.getAllCategories();

        return wrappedResponse.data();
    }

    public CategoryPageResponse<CategoryHierarchyResponse> getPagedCategories(int page, int size) {
        CommonResponse<CategoryPageResponse<CategoryHierarchyResponse>> wrappedResponse = categoryAdaptor.getPagedCategories(page, size);

        return wrappedResponse.data();
    }
}
