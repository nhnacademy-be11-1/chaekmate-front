package shop.chaekmate.front.coupon.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.coupon.dto.request.CouponIssueRequest;
import shop.chaekmate.front.coupon.dto.response.BookCouponPolicyResponse;
import shop.chaekmate.front.coupon.dto.response.CouponIssueResponse;

import java.util.List;

@FeignClient(name = "coupon-client", url = "${chaekmate.gateway.url}")
public interface CouponAdaptor {

    @GetMapping("/coupon-policies/books/{bookId}")
    CommonResponse<List<BookCouponPolicyResponse>> getAvailableCouponsForBook(
            @PathVariable Long bookId,
            @RequestParam List<Long> categoryIds,
            @RequestHeader("X-Member-Id") Long memberId
    );

    @PostMapping("/issued-coupons")
    CommonResponse<CouponIssueResponse> issueCoupon(
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestBody CouponIssueRequest request
    );
}
