package shop.chaekmate.front.book.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.BookQueryAdaptor;
import shop.chaekmate.front.book.dto.response.BookQuerySliceResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookQueryService {

    private final BookQueryAdaptor bookQueryAdaptor;

    public BookQuerySliceResponse getTopReviews30Days(Pageable pageable) {
        try {
            return bookQueryAdaptor.getTopReviews30Days(pageable).data();
        } catch (FeignException e) {
            log.warn("TopReviews30Days 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getRecent(Pageable pageable) {
        try {
            return bookQueryAdaptor.getRecent(pageable).data();
        } catch (FeignException e) {
            log.warn("Recent 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getRanking(String type, Pageable pageable) {
        try {
            return bookQueryAdaptor.getRanking(type, pageable).data();
        } catch (FeignException e) {
            log.warn("Ranking 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getPersonalRecommendations(Pageable pageable) {
        try {
            return bookQueryAdaptor.getPersonalRecommendations(pageable).data();
        } catch (FeignException e) {
            log.warn("PersonalRecommendations 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getNewReleases(Pageable pageable) {
        try {
            return bookQueryAdaptor.getNewReleases(pageable).data();
        } catch (FeignException e) {
            log.warn("NewReleases 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getEarlyAdopterPicks(Pageable pageable) {
        try {
            return bookQueryAdaptor.getEarlyAdopterPicks(pageable).data();
        } catch (FeignException e) {
            log.warn("EarlyAdopterPicks 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getChaekmatePicks(Pageable pageable) {
        try {
            return bookQueryAdaptor.getChaekmatePicks(pageable).data();
        } catch (FeignException e) {
            log.warn("ChaekmatePicks 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getBestsellers(Pageable pageable) {
        try {
            return bookQueryAdaptor.getBestsellers(pageable).data();
        } catch (FeignException e) {
            log.warn("Bestsellers 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    public BookQuerySliceResponse getAllBooks(Long categoryId,
                                              Long tagId,
                                              String keyword,
                                              Pageable pageable) {
        try {
            return bookQueryAdaptor.getAllBooks(categoryId, tagId, keyword, pageable).data();
        } catch (FeignException e) {
            log.warn("AllBooks 조회 중 에러가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }
}
