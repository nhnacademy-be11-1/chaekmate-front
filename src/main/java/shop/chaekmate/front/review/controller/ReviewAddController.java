package shop.chaekmate.front.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.book.service.BookImageService;
import shop.chaekmate.front.review.dto.request.ReviewCreateRequest;
import shop.chaekmate.front.review.dto.request.ReviewUpdateRequest;
import shop.chaekmate.front.review.dto.response.ReviewCreateResponse;
import shop.chaekmate.front.review.dto.response.ReviewUpdateResponse;
import shop.chaekmate.front.review.service.ReviewAddService;

@Controller
@RequiredArgsConstructor
public class ReviewAddController {

    private final ReviewAddService reviewAddService;
    private final BookImageService bookImageService;

    @GetMapping("/reviews/{bookId}/add")
    public String addReviewView(@PathVariable Long bookId,
                                @RequestParam(value = "orderedBookId") Long orderedBookId,
                                @AuthenticationPrincipal CustomPrincipal principal,
                                Model model){

        BookDetailResponse bookDetailResponse = reviewAddService.getBookById(bookId);
        BookThumbnailResponse thumbnail = bookImageService.getThumbnailByBookId(bookId);

        model.addAttribute("bookInfo", bookDetailResponse);
        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("orderedBookId", orderedBookId);
        model.addAttribute("memberId", principal.getMemberId());

        return "review/review-add";
    }

    @PostMapping("/reviews")
    public String createReviews(@ModelAttribute ReviewCreateRequest request,
                                @RequestParam Long bookId,
                                Model model){

        ReviewCreateResponse response = reviewAddService.createReview(request);
        if(response == null){
            model.addAttribute("error", "리뷰 등록에 실패했습니다. 다시 시도해주세요.");
            model.addAttribute("bookId", bookId);
            return "review/review-add";
        }

        if(request.imageUrls() != null && !request.imageUrls().isEmpty()){
            reviewAddService.addReviewImages(response.id(), request.imageUrls());
        }
        model.addAttribute("reviewResponse", response);
        model.addAttribute("bookId", bookId);

        return "review/review-complete";
    }

    @PutMapping("/reviews/{reviewId}")
    public String updateReview(@PathVariable Long reviewId,
                              @ModelAttribute ReviewUpdateRequest request,
                              @RequestParam Long bookId,
                              Model model){

        ReviewUpdateResponse response = reviewAddService.updateReview(reviewId, request);
        if(response == null){
            model.addAttribute("error", "리뷰 수정에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/books/" + bookId + "?error=review_update_failed";
        }

        if(request.imageUrls() != null && !request.imageUrls().isEmpty()){
            reviewAddService.addReviewImages(response.id(), request.imageUrls());
        }

        return "redirect:/books/" + bookId;
    }

}
