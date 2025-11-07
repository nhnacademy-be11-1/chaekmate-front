package shop.chaekmate.front.book.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.book.service.AdminBookService;

@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminBookService bookService;

    // 도서 관리자 페이지
    @GetMapping("/admin/books")
    public String bookManagementView(Model model) {

        List<AdminBookResponse> recentBooks = bookService.getRecentCreatedBooks(5);
        model.addAttribute("recentBooks",recentBooks);

        return "admin/book/book-management";
    }

    // 도서 직접 추가 페이지 뷰
    @GetMapping("/admin/books/add-direct")
    public String bookManagementAddDirectView(@ModelAttribute BookCreateRequest request, Model model) {
        return "admin/book/book-management-add-direct";
    }

    // 알라딘 도서 검색 페이지
    @GetMapping("/admin/books/add-aladin")
    public String bookManagementAddAladinView(Model model) {
        return "admin/book/book-management-add-aladin";
    }

    // 도서 추가 요청
    @PostMapping("/admin/books")
    public String createBook(@ModelAttribute BookCreateRequest request) {

        bookService.createBook(request);
        return "redirect:/admin/books";
    }

    // 도서 상세 페이지
    @GetMapping("/admin/books/{bookId}")
    public String bookManagementDetailView(@PathVariable Long bookId, Model model) {


        return "admin/book/book-management-detail";
    }

    // 관리자 도서 수정 페이지
    @GetMapping("/admin/books/{bookId}/modify")
    public String bookManagementModifyView(@PathVariable Long bookId, Model model) {
        return "admin/book/book-management-modify";
    }

}
