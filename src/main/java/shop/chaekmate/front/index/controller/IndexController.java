package shop.chaekmate.front.index.controller;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.book.dto.response.BookQueryResponse;
import shop.chaekmate.front.book.dto.response.BookQuerySliceResponse;
import shop.chaekmate.front.book.service.BookQueryService;
import shop.chaekmate.front.index.dto.BookSliceInfo;
import shop.chaekmate.front.index.dto.Slide;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final BookQueryService bookQueryService;

    @GetMapping("/")
    public String index(Model model) {

        Pageable pageable = PageRequest.of(0, 20);

        // 실패하면 null 이 반환됨
        BookQuerySliceResponse chaekmatePicksWrapped = bookQueryService.getChaekmatePicks(pageable);
        BookQuerySliceResponse recentBooksWrapped = bookQueryService.getRecent(pageable);
        BookQuerySliceResponse recommendedBooksWrapped = bookQueryService.getPersonalRecommendations(pageable);
        BookQuerySliceResponse mostReviewedBooksWrapped = bookQueryService.getTopReviews30Days(pageable);
        BookQuerySliceResponse earlyAdopterPicksWrapped = bookQueryService.getEarlyAdopterPicks(pageable);
        BookQuerySliceResponse mostViewedBooksWrapped = bookQueryService.getRanking("VIEWS", pageable);

        // ===== 슬라이드 처리 =====
        Slide slide1 = null;
        Slide slide2 = null;
        Slide slide3 = null;

        if (chaekmatePicksWrapped != null && !chaekmatePicksWrapped.getContent().isEmpty()) {
            List<BookQueryResponse> list = chaekmatePicksWrapped.getContent();

            slide1 = Slide.of("책메이트 추천", "책메이트 선정 추천 도서", list.getFirst());

            if (list.size() > 1) {
                slide2 = Slide.of("이달의 추천도서",
                        String.format("%s 의 대표작", list.getLast().getAuthor()),
                        list.getLast());
            }
        }

        if (mostViewedBooksWrapped != null && !mostViewedBooksWrapped.getContent().isEmpty()) {
            slide3 = Slide.of(
                    "실시간 조회수 급상승",
                    "지금, 우리에게 필요한 메시지",
                    mostViewedBooksWrapped.getContent().getFirst()
            );
        }

        model.addAttribute("slide1", slide1);
        model.addAttribute("slide2", slide2);
        model.addAttribute("slide3", slide3);

        // ===== 목록 처리 (null → null) =====
        model.addAttribute("recentBooks",
                toBookSliceInfoListOrNull(recentBooksWrapped));

        model.addAttribute("recommendedBooks",
                toBookSliceInfoListOrNull(recommendedBooksWrapped));

        model.addAttribute("mostReviewedBooks",
                toBookSliceInfoListOrNull(mostReviewedBooksWrapped));

        model.addAttribute("earlyAdopterPicks",
                toBookSliceInfoListOrNull(earlyAdopterPicksWrapped));

        return "index";
    }

    // ===== 유틸 =====
    private List<BookSliceInfo> toBookSliceInfoListOrNull(BookQuerySliceResponse response) {
        if (response == null || response.getContent() == null) return Collections.emptyList();
        return response.getContent().stream().map(BookSliceInfo::of).toList();
    }

    @GetMapping("/admin")
    public String adminIndex(Model model){

        return "admin/admin-index";
    }

}
