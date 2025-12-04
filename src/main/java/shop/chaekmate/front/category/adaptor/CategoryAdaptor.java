package shop.chaekmate.front.category.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.category.dto.request.CategoryCreateRequest;
import shop.chaekmate.front.category.dto.response.CategoryCreateResponse;
import shop.chaekmate.front.category.dto.response.CategoryHierarchyResponse;
import shop.chaekmate.front.category.dto.response.CategoryPageResponse;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.common.CommonResponse;

import java.util.List;

@FeignClient(name="category-client" , url = "${chaekmate.gateway.url}")
public interface CategoryAdaptor {

    @GetMapping(value= "/categories")
    CommonResponse<List<CategoryResponse>> getAllCategories();

    @GetMapping(value = "/categories/paged")
    CommonResponse<CategoryPageResponse<CategoryHierarchyResponse>> getPagedCategories(@RequestParam("page") int page, @RequestParam("size") int size);

    @DeleteMapping(value ="/admin/categories/{id}")
    CommonResponse<Void> deleteCategory(@PathVariable("id") Long id);

    @PostMapping(value = "/admin/categories")
    CommonResponse<CategoryCreateResponse> createCategory(@RequestBody CategoryCreateRequest request);

}
