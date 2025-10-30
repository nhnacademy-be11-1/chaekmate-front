package shop.chaekmate.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.dto.Book;
import shop.chaekmate.front.dto.Tag;
import shop.chaekmate.front.dto.request.BookCreateRequest;
import shop.chaekmate.front.dto.request.BookUpdateRequest;
import shop.chaekmate.front.dto.response.CategoryResponse;
import org.springframework.web.bind.annotation.RequestBody; // Added

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class BookAdminController {

    private final CoreClient coreClient;

    @GetMapping
    public String manageBooks(Model model) {
        model.addAttribute("books", coreClient.getRecentBooks(5)); // Fetch recent 5 books
        model.addAttribute("bookCreateRequest", new BookCreateRequest()); // Initialize for form
        return "admin/manage-books";
    }

    @PostMapping
    public String addBook(@RequestBody BookCreateRequest bookCreateRequest,
                          RedirectAttributes redirectAttributes) {
        try {
            coreClient.createBook(bookCreateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "새 도서가 추가되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "도서 추가 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/{bookId}")
    public String updateBook(@PathVariable Long bookId,
                             @RequestBody BookUpdateRequest bookUpdateRequest,
                             RedirectAttributes redirectAttributes) {
        try {
            coreClient.updateBook(bookId, bookUpdateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "도서가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "도서 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/books/" + bookId;
    }

    @PostMapping("/{bookId}/delete")
    public String deleteBook(@PathVariable Long bookId, RedirectAttributes redirectAttributes) {
        try {
            coreClient.deleteBook(bookId);
            redirectAttributes.addFlashAttribute("successMessage", "도서가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "도서 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/{bookId}")
    public String adminBookDetail(@PathVariable Long bookId, Model model) {
        Book book = coreClient.getBookById(bookId);

        // Fetch all tags and categories
        List<Tag> allTags = coreClient.getAllTags();
        List<CategoryResponse> allCategories = coreClient.getAllCategories(); // Keep hierarchical

        // Create a map for category ID to name for easier lookup in Thymeleaf
        Map<Long, String> categoryIdToNameMap = new HashMap<>();
        flattenCategoriesToMap(allCategories, categoryIdToNameMap);

        // Create a BookUpdateRequest from the Book object and its tag/category IDs
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(
                book.id(),
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
                book.isWrappeable(),
                book.isSaleEnd(),
                book.imageUrl(), // Added imageUrl
                book.tagIds(), // Directly use tag IDs
                book.categoryIds() // Directly use category IDs
        );

        model.addAttribute("book", book); // Keep original book for display if needed
        model.addAttribute("bookUpdateRequest", bookUpdateRequest); // For the form
        model.addAttribute("allCategories", allCategories); // Pass hierarchical categories for modal
        model.addAttribute("allTags", allTags); // For selection in form
        model.addAttribute("categoryIdToNameMap", categoryIdToNameMap); // For displaying selected categories
        return "admin/admin-book-detail";
    }


    // Helper method to flatten hierarchical categories into a map of ID to name
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
