package shop.chaekmate.front.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.review.adaptor.ReviewAdaptor;
import shop.chaekmate.front.review.dto.response.ReviewResponse;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewAdaptor reviewAdaptor;

    public Page<ReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable) {
        CommonResponse<Page<ReviewResponse>> response =
                reviewAdaptor.getReviewsByBookId(bookId, pageable);
        return response.data();
    }
}

