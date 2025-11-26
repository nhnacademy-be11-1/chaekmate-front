package shop.chaekmate.front.review.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.review.dto.response.ReviewResponse;

@FeignClient(name = "review-client-get", url = "${chaekmate.gateway.url}")
public interface ReviewAdaptor {

    @GetMapping(value = "/books/{bookId}/reviews")
    CommonResponse<Page<ReviewResponse>> getReviewsByBookId(
            @PathVariable("bookId") Long bookId,
            @SpringQueryMap Pageable pageable);
}

