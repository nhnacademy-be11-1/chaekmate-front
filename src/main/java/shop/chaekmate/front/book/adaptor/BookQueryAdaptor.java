package shop.chaekmate.front.book.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.book.dto.response.BookQuerySliceResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "bookQuery-client", url = "${chaekmate.gateway.url}")
public interface BookQueryAdaptor {

    @GetMapping("/books/top-reviews-30days")
    CommonResponse<BookQuerySliceResponse> getTopReviews30Days(@PageableDefault Pageable pageable);

    @GetMapping("/books/recent")
    CommonResponse<BookQuerySliceResponse> getRecent(@PageableDefault Pageable pageable);

    @GetMapping("/books/ranking")
    CommonResponse<BookQuerySliceResponse> getRanking(@RequestParam String type, @PageableDefault Pageable pageable);

    @GetMapping("/books/personal-recommendations")
    CommonResponse<BookQuerySliceResponse> getPersonalRecommendations(@PageableDefault Pageable pageable);

    @GetMapping("/books/new-releases")
    CommonResponse<BookQuerySliceResponse> getNewReleases(@PageableDefault Pageable pageable);

    @GetMapping("/books/early-adopter-picks")
    CommonResponse<BookQuerySliceResponse> getEarlyAdopterPicks(@PageableDefault Pageable pageable);

    @GetMapping("/books/chaekmate-picks")
    CommonResponse<BookQuerySliceResponse> getChaekmatePicks(@PageableDefault Pageable pageable);

    @GetMapping("/books/bestsellers")
    CommonResponse<BookQuerySliceResponse> getBestsellers(@PageableDefault Pageable pageable);

    @GetMapping("/books/all")
    CommonResponse<BookQuerySliceResponse> getAllBooks(@RequestParam(required = false) Long categoryId,
                                                       @RequestParam(required = false) Long tagId,
                                                       @RequestParam(required = false) String keyword,
                                                       @PageableDefault Pageable pageable);
}
