package shop.chaekmate.front.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.category.adaptor.CategoryAdaptor;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.common.CommonResponse;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryAdaptor categoryAdaptor;

    public List<CategoryResponse> getCategories() {

        CommonResponse<List<CategoryResponse>> wrappedResponse = categoryAdaptor.getAllCategories();

        return wrappedResponse.data();
    }
}
