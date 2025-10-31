package shop.chaekmate.front.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.book.entity.Book;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.request.BookUpdateRequest;
import shop.chaekmate.front.book.dto.request.CategoryCreateRequest;
import shop.chaekmate.front.book.dto.request.TagCreateRequest;
import shop.chaekmate.front.book.dto.response.CategoryCreateResponse;
import shop.chaekmate.front.book.dto.response.CategoryResponse;
import shop.chaekmate.front.book.entity.Tag;
import shop.chaekmate.front.book.dto.response.TagCreateResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@FeignClient(name = "core-server", url = "${chaekmate.core.url}")
public interface CoreClient {

    @PostMapping(value = "/admin/books")
    void createBook(@RequestBody BookCreateRequest bookCreateRequest);

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

    @GetMapping("/tags")
    List<Tag> getAllTags();

    @DeleteMapping("/admin/tags/{tagId}")
    void deleteTag(@PathVariable("tagId") Long tagId);

    @PutMapping("/admin/tags/{tagId}")
    void updateTag(@PathVariable("tagId") Long tagId, TagCreateRequest tagCreateRequest);

    @PutMapping(value = "/books/{bookId}")
    void updateBook(@PathVariable("bookId") Long bookId, BookUpdateRequest bookUpdateRequest);

    @DeleteMapping("/books/{bookId}")
    void deleteBook(@PathVariable("bookId") Long bookId);

    @GetMapping("/admin/books/recent")
    List<Book> getRecentBooks(@RequestParam("limit") int limit);

    @GetMapping("/books/{bookId}")
    Book getBookById(@PathVariable("bookId") Long bookId);
}
