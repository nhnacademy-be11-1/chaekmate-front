package shop.chaekmate.front.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.review.adaptor.ReviewAdaptor;
import shop.chaekmate.front.review.dto.response.ReviewResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewAdaptor reviewAdaptor;

    public Page<ReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable) {
        CommonResponse<Page<ReviewResponse>> response =
                reviewAdaptor.getReviewsByBookId(bookId, pageable);
        return response.data();
    }

    public Map<Long, Boolean> checkReviewExistsByOrderedBookIds(Map<Long, Long> orderedBookIdToBookIdMap) {
        Map<Long, Boolean> result = new HashMap<>();

        // 각 bookId별로 리뷰를 조회하고, orderedBookId가 일치하는지 확인
        Map<Long, Set<Long>> bookIdToOrderedBookIds = orderedBookIdToBookIdMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toSet())
                ));

        for (Map.Entry<Long, Set<Long>> entry : bookIdToOrderedBookIds.entrySet()) {
            Long bookId = entry.getKey();
            Set<Long> orderedBookIds = entry.getValue();

            try {
                // bookId로 리뷰 조회 (최대 100개, 최신순)
                Page<ReviewResponse> reviews = getReviewsByBookId(bookId, PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdAt")));

                // orderedBookId가 일치하는 리뷰가 있는지 확인
                Set<Long> reviewedOrderedBookIds = reviews.getContent().stream()
                        .map(ReviewResponse::orderedBookId)
                        .filter(orderedBookIds::contains)
                        .collect(Collectors.toSet());

                for (Long orderedBookId : orderedBookIds) {
                    result.put(orderedBookId, reviewedOrderedBookIds.contains(orderedBookId));
                }
            } catch (Exception e) {
                for (Long orderedBookId : orderedBookIds) {
                    result.put(orderedBookId, false);
                }
            }
        }

        return result;
    }
}

