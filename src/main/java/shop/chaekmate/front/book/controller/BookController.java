package shop.chaekmate.front.book.controller;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.book.adaptor.BookAdaptor;
import shop.chaekmate.front.book.adaptor.BookViewCountAdaptor;
import shop.chaekmate.front.book.dto.response.BookImageResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.book.dto.BookListResponse;
import shop.chaekmate.front.book.service.BookImageService;
import shop.chaekmate.front.book.service.LikeService;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.review.dto.response.ReviewResponse;
import shop.chaekmate.front.review.service.ReviewService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookAdaptor bookAdaptor;
    private final BookViewCountAdaptor bookViewCountAdaptor;
    private final BookImageService bookImageService;
    private final LikeService likeService;
    private final ReviewService reviewService;

    @GetMapping("/categories/{categoryId}")
    public String getBookByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal CustomPrincipal principal,
            Model model) {
        CommonResponse<Page<BookListResponse>> response = bookAdaptor.getBooksByCategory(categoryId, null, null, page, size);

        Page<BookListResponse> books = response.data();

        if(Objects.nonNull(principal) && Objects.nonNull(principal.getRole())) {
            // 회원 좋아요 여부 확인용
            List<Long> likedBookIds = likeService.getMemberLikedBook();
            model.addAttribute("likedBookIds", likedBookIds);
        }

        model.addAttribute("books", books.getContent());
        model.addAttribute("currentPage", books.getNumber());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("title", "도서 목록");

        return "book/book-category-list";
    }

    @GetMapping("/books/{bookId}")
    public String getBookDetail(
            @PathVariable Long bookId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomPrincipal principal,
            Model model) {
        CommonResponse<BookDetailResponse> response = bookAdaptor.getBookById(bookId);
        BookDetailResponse bookDetailResponse = response.data();

        BookThumbnailResponse thumbnail = bookImageService.getThumbnailByBookId(bookId);

        List<BookImageResponse> detailImages = bookImageService.getDetailImagesByBookId(bookId);

        //조회수 증가 요청
        bookViewCountAdaptor.increaseView(bookId);

        // 리뷰 조회
        Page<ReviewResponse> reviews = reviewService.getReviewsByBookId(bookId, pageable);

        if(Objects.nonNull(principal) && Objects.nonNull(principal.getRole())) {
            // 회원 좋아요 여부 확인용
            List<Long> likedBookIds = likeService.getMemberLikedBook();
            model.addAttribute("likedBookIds", likedBookIds);
        }

        model.addAttribute("book", bookDetailResponse);
        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("detailImages", detailImages);
        model.addAttribute("reviews", reviews);
        model.addAttribute("title", bookDetailResponse.title());

        return "book/book-detail";
    }
}

