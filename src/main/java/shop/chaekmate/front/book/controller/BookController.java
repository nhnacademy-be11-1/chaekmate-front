package shop.chaekmate.front.book.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.book.adaptor.BookAdaptor;
import shop.chaekmate.front.book.adaptor.BookViewCountAdaptor;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.book.dto.BookListResponse;
import shop.chaekmate.front.book.dto.response.BookImageResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.book.dto.BookListResponse;
import shop.chaekmate.front.book.service.BookImageService;
import shop.chaekmate.front.book.service.LikeService;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.coupon.adaptor.CouponAdaptor;
import shop.chaekmate.front.coupon.dto.response.BookCouponPolicyResponse;
import shop.chaekmate.front.review.dto.response.ReviewResponse;
import shop.chaekmate.front.review.service.ReviewService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookAdaptor bookAdaptor;
    private final BookViewCountAdaptor bookViewCountAdaptor;
    private final BookImageService bookImageService;
    private final LikeService likeService;
    private final ReviewService reviewService;
    private final CouponAdaptor couponAdaptor;

    @GetMapping("/categories/{categoryId}")
    public String getBookByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        CommonResponse<Page<BookListResponse>> response = bookAdaptor.getBooksByCategory(categoryId, null, null, page, size);

        Page<BookListResponse> books = response.data();

        // 좋아요 여부 확인용
        List<Long> likedBookIds = likeService.getMemberLikedBook();

        model.addAttribute("likedBookIds", likedBookIds);
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
            @AuthenticationPrincipal CustomPrincipal principal,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        // 1. 도서 상세 정보 조회
        CommonResponse<BookDetailResponse> response = bookAdaptor.getBookById(bookId);
        BookDetailResponse bookDetailResponse = response.data();

        BookThumbnailResponse thumbnail = bookImageService.getThumbnailByBookId(bookId);

        List<BookImageResponse> detailImages = bookImageService.getDetailImagesByBookId(bookId);

        // 조회수 증가 요청
        bookViewCountAdaptor.increaseView(bookId);

        // 좋아요 여부 확인
        List<Long> likedBookIds = likeService.getMemberLikedBook();

        // 리뷰 조회
        Page<ReviewResponse> reviews = reviewService.getReviewsByBookId(bookId, pageable);

        // 2. 쿠폰 조회 (로그인한 경우에만)
        List<BookCouponPolicyResponse> coupons = List.of();
        if (principal != null) {
            try {
                CommonResponse<List<BookCouponPolicyResponse>> couponResponse = couponAdaptor.getAvailableCouponsForBook(
                        bookId,
                        bookDetailResponse.categoryIds(),
                        principal.getMemberId()
                );
                coupons = couponResponse.data();
                log.info("쿠폰 조회 성공! 개수: " + coupons.size());
            } catch (Exception e) {
                log.info("쿠폰 조회 실패: " + e.getMessage());
            }
        }

        model.addAttribute("likedBookIds", likedBookIds);
        model.addAttribute("book", bookDetailResponse);
        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("detailImages", detailImages);
        model.addAttribute("reviews", reviews);
        model.addAttribute("coupons", coupons);
        model.addAttribute("title", bookDetailResponse.title());

        return "book/book-detail";
    }
}

