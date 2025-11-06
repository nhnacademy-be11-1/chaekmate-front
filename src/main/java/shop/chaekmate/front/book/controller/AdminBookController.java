package shop.chaekmate.front.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.service.AdminBookService;

@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminBookService bookService;

    @GetMapping("/admin/books")
    public String bookManagementView(Model model) {



        return "admin/book/book-management";
    }

    @GetMapping("/admin/books/add-direct")
    public String bookManagementAddDirectView(@ModelAttribute BookCreateRequest request, Model model) {
        return "admin/book/book-management-add-direct";
    }

    @GetMapping("/admin/books/add-aladin")
    public String bookManagementAddAladinView(Model model) {
        return "admin/book/book-management-add-aladin";
    }

    @PostMapping("/admin/books")
    public String createBook(@ModelAttribute BookCreateRequest request) {

        bookService.createBook(request);
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/books/{bookId}")
    public String bookManagementDetailView(@PathVariable Long bookId, Model model) {
        return "admin/book/book-management-detail";
    }

    @GetMapping("/admin/books/{bookId}/modify")
    public String bookManagementModifyView(@PathVariable Long bookId, Model model) {
        return "admin/book/book-management-modify";
    }

}
