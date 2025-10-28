package shop.chaekmate.front.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.dto.request.BookCreateRequest;
import shop.chaekmate.front.dto.request.CategoryCreateRequest;
import shop.chaekmate.front.dto.request.TagCreateRequest;
import shop.chaekmate.front.dto.response.CategoryCreateResponse;
import shop.chaekmate.front.dto.response.CategoryResponse;
import shop.chaekmate.front.dto.response.TagCreateResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@FeignClient(name = "core-server", url = "${chaekmate.core.url}")
public interface CoreClient {

    @PostMapping(value = "/admin/books", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void createBook(@RequestPart("bookCreateRequest") BookCreateRequest bookCreateRequest,
                    @RequestPart("image") MultipartFile image);

    @PostMapping(value = "/admin/tags")
    TagCreateResponse createTag(TagCreateRequest tagCreateRequest);

    @PostMapping("/admin/categories")
    CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest);

    @GetMapping("/categories")
    List<CategoryResponse> getAllCategories();

    @PutMapping("/admin/categories/{categoryId}")
    void updateCategory(@PathVariable("categoryId") Long categoryId, CategoryCreateRequest categoryCreateRequest);

    @DeleteMapping("/admin/categories/{categoryId}")
    void deleteCategory(@PathVariable("categoryId") Long categoryId);
}
