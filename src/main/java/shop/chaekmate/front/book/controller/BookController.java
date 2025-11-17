package shop.chaekmate.front.book.controller;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.book.adaptor.BookAdaptor;
import shop.chaekmate.front.book.adaptor.BookImageAdaptor;
import shop.chaekmate.front.book.adaptor.BookViewCountAdaptor;
import shop.chaekmate.front.book.dto.response.BookImageResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.book.dto.BookListResponse;
import shop.chaekmate.front.common.CommonResponse;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookAdaptor bookAdaptor;
    private final BookImageAdaptor bookImageAdaptor;
    private final BookViewCountAdaptor bookViewCountAdaptor;

    @GetMapping("/categories/{categoryId}")
    public String getBookByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        CommonResponse<Page<BookListResponse>> response = bookAdaptor.getBooksByCategory(categoryId, null, null, page, size);

        Page<BookListResponse> books = response.data();

        model.addAttribute("books", books.getContent());
        model.addAttribute("currentPage", books.getNumber());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("title", "도서 목록");

        return "book/book-list";
    }

    @GetMapping("/books/{bookId}")
    public String getBookDetail(@PathVariable Long bookId, Model model) {
        CommonResponse<BookDetailResponse> response = bookAdaptor.getBookById(bookId);
        BookDetailResponse bookDetailResponse = response.data();

        CommonResponse<BookThumbnailResponse> thumbnailResponse = bookImageAdaptor.getBookThumbnail(bookId);
        BookThumbnailResponse thumbnail = thumbnailResponse.data();

        CommonResponse<List<BookImageResponse>> detailImagesResponse = bookImageAdaptor.getBookDetailImages(bookId);
        List<BookImageResponse> detailImages = detailImagesResponse.data() != null ? detailImagesResponse.data() : Collections.emptyList();

        //조회수 증가 요청
        bookViewCountAdaptor.increaseView(bookId);

        model.addAttribute("book", bookDetailResponse);
        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("detailImages", detailImages);
        model.addAttribute("title", bookDetailResponse.title());

        return "book/book-detail";
    }
}
