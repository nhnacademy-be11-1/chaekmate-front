package shop.chaekmate.front.book.controller;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.response.CategoryResponse;
import shop.chaekmate.front.book.entity.Book;
import shop.chaekmate.front.book.entity.Tag;
import shop.chaekmate.front.common.CoreClient;

@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final CoreClient coreClient;

    @GetMapping("/admin/books")
    public String manageBooks(Model model) {
        model.addAttribute("books", coreClient.getRecentBooks(5));
        return "admin/manage-books";
    }

    @GetMapping("/admin/books/add")
    public String addBookForm(Model model) {
        // Tag 들을 가져옴
        List<Tag> allTags = coreClient.getAllTags();

        // 카테고리 ID들을 name으로 바꿈
        Map<Long, String> categoryIdToNameMap = new HashMap<>();
        List<CategoryResponse> allCategories = coreClient.getAllCategories();
        flattenCategoriesToMap(allCategories, categoryIdToNameMap);

        model.addAttribute("allTags", allTags); // For selection in form
        model.addAttribute("categoryIdToNameMap", categoryIdToNameMap); // For displaying selected categories
        model.addAttribute("allCategories", allCategories);
        return "admin/add-book";
    }

    @PostMapping("/admin/books")
    public String addBook(@Valid @RequestBody BookCreateRequest bookCreateRequest, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
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
    public String updateBook(@PathVariable Long bookId, @Valid BookCreateRequest bookUpdateRequest,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "입력값이 유효하지 않습니다. " + bindingResult.getFieldError());
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
        List<CategoryResponse> allCategories = coreClient.getAllCategories();

        // Create a map for category ID to name for easier lookup in Thymeleaf
        Map<Long, String> categoryIdToNameMap = new HashMap<>();
        flattenCategoriesToMap(allCategories, categoryIdToNameMap);

        model.addAttribute("book", book); // Keep original book for display if needed
        model.addAttribute("allTags", allTags); // For selection in form
        model.addAttribute("categoryIdToNameMap", categoryIdToNameMap); // For displaying selected categories
        model.addAttribute("allCategories", allCategories);
        return "admin/admin-book-detail";
    }

    // Helper method
    private void flattenCategoriesToMap(List<CategoryResponse> categories, Map<Long, String> map) {
        if (categories == null) {
            return;
        }
        for (CategoryResponse category : categories) {
            map.put(category.id(), category.name());
            if (category.children() != null && !category.children().isEmpty()) {
                flattenCategoriesToMap(category.children(), map);
            }
        }
    }

}
