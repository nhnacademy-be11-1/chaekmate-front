package shop.chaekmate.front.book.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.book.dto.response.AdminBookDetail;
import shop.chaekmate.front.book.service.AdminBookService;

@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminBookService adminBookService;

    // 도서 관리자 페이지 뷰
    @GetMapping("/admin/books")
    public String bookManagementView(Model model) {

        List<AdminBookResponse> recentBooks = adminBookService.getRecentCreatedBooks(5);
        model.addAttribute("recentBooks",recentBooks);

        return "admin/book/book-management";
    }

    // 도서 직접 추가 페이지 뷰
    @GetMapping("/admin/books/new")
    public String bookManagementAddDirectView(@ModelAttribute BookCreateRequest bookCreateRequest, Model model) {
        model.addAttribute("bookCreateRequest", bookCreateRequest);

        return "admin/book/book-management-add-direct";
    }

    // 알라딘 도서 검색 페이지 뷰
    @GetMapping("/admin/books/aladin")
    public String bookManagementAddAladinView(Model model) {

        return "admin/book/book-management-add-aladin";
    }

    // 도서 추가 요청
    @PostMapping("/admin/books")
    public String createBook(@ModelAttribute BookCreateRequest request) {

        adminBookService.createBook(request);
        return "redirect:/admin/books";
    }

    // 도서 상세 페이지
    @GetMapping("/admin/books/{bookId}")
    public String bookManagementDetailView(@PathVariable Long bookId, Model model) {

        AdminBookDetail book = adminBookService.getBookById(bookId);
        model.addAttribute("book", book);

        return "admin/book/book-management-detail";
    }

    // 관리자 도서 수정 페이지
    @GetMapping("/admin/books/{bookId}/modify")
    public String bookManagementModifyView(@PathVariable Long bookId, Model model) {

        AdminBookDetail book = adminBookService.getBookById(bookId);
        model.addAttribute("book", book);

        return "admin/book/book-management-modify";
    }

    // 관리자 도서 수정 요청
    @PutMapping("/admin/books/{bookId}/modify")
    public String modifyBook(@PathVariable Long bookId,
                             @ModelAttribute AdminBookDetail adminBookDetail){ // @RequestParam 과 비슷

        adminBookService.modifyBookByBookDetail(bookId, adminBookDetail);

        return "redirect:/admin/books";
    }

}
