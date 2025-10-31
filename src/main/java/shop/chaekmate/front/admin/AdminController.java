package shop.chaekmate.front.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.dto.Book;
import shop.chaekmate.front.dto.Tag;
import shop.chaekmate.front.dto.request.BookCreateRequest;
import shop.chaekmate.front.dto.request.BookUpdateRequest;
import shop.chaekmate.front.dto.request.CategoryCreateRequest;
import shop.chaekmate.front.dto.request.TagCreateRequest;
import shop.chaekmate.front.dto.response.CategoryResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CoreClient coreClient;

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin/index";
    }

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }

    // Book Admin Methods
    @GetMapping("/admin/books")
    public String manageBooks(Model model) {
        model.addAttribute("books", coreClient.getRecentBooks(5));
        model.addAttribute("bookCreateRequest", new BookCreateRequest());
        return "admin/manage-books";
    }

    @PostMapping("/admin/books")
    public String addBook(@Valid @RequestBody BookCreateRequest bookCreateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력값이 유효하지 않습니다. " + bindingResult);
            return "redirect:/admin/books";
        }
        try {
            coreClient.createBook(bookCreateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "새 도서가 추가되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "도서 추가 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/books";
    }

    @PutMapping("/admin/books/{bookId}")
    public String updateBook(@PathVariable Long bookId, @Valid BookUpdateRequest bookUpdateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력값이 유효하지 않습니다. " + bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/admin/books/" + bookId;
        }
        try {
            coreClient.updateBook(bookId, bookUpdateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "도서가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "도서 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/books/" + bookId;
    }

    @PostMapping("/admin/books/{bookId}/delete")
    public String deleteBook(@PathVariable Long bookId, RedirectAttributes redirectAttributes) {
        try {
            coreClient.deleteBook(bookId);
            redirectAttributes.addFlashAttribute("successMessage", "도서가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "도서 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/books/{bookId}")
    public String adminBookDetail(@PathVariable Long bookId, Model model) {
        Book book = coreClient.getBookById(bookId);
        // Fetch all tags
        List<Tag> allTags = coreClient.getAllTags();

        // Create a map for category ID to name for easier lookup in Thymeleaf
        Map<Long, String> categoryIdToNameMap = new HashMap<>();
        flattenCategoriesToMap((List<CategoryResponse>) model.getAttribute("allCategories"), categoryIdToNameMap);

        // Create a BookUpdateRequest from the Book object and its tag/category IDs
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(
                book.title(),
                book.author(),
                book.publisher(),
                book.publishedAt(),
                book.isbn(),
                book.description(),
                book.index(),
                book.price(),
                book.salesPrice(),
                book.stock(),
                book.isWrappable(),
                book.isSaleEnd(),
                book.imageUrl(),
                book.tagIds(),
                book.categoryIds()
        );

        model.addAttribute("book", book); // Keep original book for display if needed
        model.addAttribute("bookUpdateRequest", bookUpdateRequest); // For the form
        model.addAttribute("allTags", allTags); // For selection in form
        model.addAttribute("categoryIdToNameMap", categoryIdToNameMap); // For displaying selected categories
        return "admin/admin-book-detail";
    }

    // Category Admin Methods
    @GetMapping("/admin/categories")
    public String categoryManagementForm(Model model) {
        model.addAttribute("allCategories", coreClient.getAllCategories());
        model.addAttribute("categoryCreateRequest", new CategoryCreateRequest());
        return "admin/manage-categories";
    }

    @PostMapping("/admin/categories")
    public String addCategory(@Valid CategoryCreateRequest categoryCreateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력값이 유효하지 않습니다. 이름은 255자를 초과할 수 없습니다.");
            return "redirect:/admin/categories";
        }
        coreClient.createCategory(categoryCreateRequest);
        redirectAttributes.addFlashAttribute("successMessage", "새 카테고리가 추가되었습니다.");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{categoryId}/update")
    public String updateCategory(@PathVariable Long categoryId, @Valid CategoryCreateRequest categoryCreateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력값이 유효하지 않습니다. 이름은 255자를 초과할 수 없습니다.");
            return "redirect:/admin/categories";
        }
        coreClient.updateCategory(categoryId, categoryCreateRequest);
        redirectAttributes.addFlashAttribute("successMessage", "카테고리가 수정되었습니다.");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{categoryId}/delete")
    public String deleteCategory(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            coreClient.deleteCategory(categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "카테고리 삭제 중 오류가 발생했습니다. 하위 카테고리가 없는지 확인해주세요.");
        }
        return "redirect:/admin/categories";
    }

    // Tag Admin Methods
    @GetMapping("/admin/tags")
    public String manageTags(Model model) {
        model.addAttribute("tags", coreClient.getAllTags());
        return "admin/manage-tags";
    }

    @PostMapping("/admin/tags")
    public String addTag(TagCreateRequest tagCreateRequest, RedirectAttributes redirectAttributes) {
        coreClient.createTag(tagCreateRequest);
        redirectAttributes.addFlashAttribute("successMessage", "새 태그가 추가되었습니다.");
        return "redirect:/admin/tags";
    }

    @PostMapping("/admin/tags/{tagId}/delete")
    public String deleteTag(@PathVariable Long tagId, RedirectAttributes redirectAttributes) {
        try {
            coreClient.deleteTag(tagId);
            redirectAttributes.addFlashAttribute("successMessage", "태그가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "태그 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/admin/tags/{tagId}/update")
    public String updateTag(@PathVariable Long tagId, TagCreateRequest tagCreateRequest, RedirectAttributes redirectAttributes) {
        try {
            coreClient.updateTag(tagId, tagCreateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "태그가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "태그 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/tags";
    }

    // Helper method
    private void flattenCategoriesToMap(List<CategoryResponse> categories, Map<Long, String> map) {
        if (categories == null) return;
        for (CategoryResponse category : categories) {
            map.put(category.getId(), category.getName());
            if (category.getChildren() != null && !category.getChildren().isEmpty()) {
                flattenCategoriesToMap(category.getChildren(), map);
            }
        }
    }
}
