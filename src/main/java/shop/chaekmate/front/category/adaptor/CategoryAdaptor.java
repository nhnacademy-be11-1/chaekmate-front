package shop.chaekmate.front.category.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name="category-client" , url = "${chaekmate.gateway.url}")
public interface CategoryAdaptor {

    @GetMapping("/categories")
    CommonResponse<List<CategoryResponse>> getAllCategories();
}
