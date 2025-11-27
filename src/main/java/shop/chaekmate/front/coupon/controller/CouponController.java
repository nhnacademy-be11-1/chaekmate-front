package shop.chaekmate.front.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.coupon.adaptor.CouponAdaptor;
import shop.chaekmate.front.coupon.dto.request.CouponIssueRequest;
import shop.chaekmate.front.coupon.dto.response.CouponIssueResponse;

@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponAdaptor couponAdaptor;

    @PostMapping("/issued-coupons")
    @ResponseBody
    public CouponIssueResponse issueCoupon(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody CouponIssueRequest request) {

        Long memberId = principal != null ? principal.getMemberId() : null;

        if (memberId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        CommonResponse<CouponIssueResponse> response = couponAdaptor.issueCoupon(memberId, request);
        return response.data();
    }
}
