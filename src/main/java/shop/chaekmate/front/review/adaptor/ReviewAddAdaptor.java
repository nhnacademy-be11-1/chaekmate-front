package shop.chaekmate.front.review.adaptor;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.review.dto.request.ReviewCreateRequest;
import shop.chaekmate.front.review.dto.request.ReviewImageAddRequest;
import shop.chaekmate.front.review.dto.response.ReviewCreateResponse;
import shop.chaekmate.front.review.dto.response.ReviewImageResponse;

@FeignClient(name = "review-client", url = "${chaekmate.gateway.url}")
public interface ReviewAddAdaptor {

    @PostMapping("/reviews")
    CommonResponse<ReviewCreateResponse> createReview(@RequestBody ReviewCreateRequest request);

    // 리뷰 이미지 등록 (여러개 한번에)
    @PostMapping("/reviews/{reviewId}/images")
    CommonResponse<List<ReviewImageResponse>> addReviewImages(@PathVariable Long reviewId,
                                                              @RequestBody ReviewImageAddRequest request);

}
