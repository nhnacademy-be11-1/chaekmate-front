package shop.chaekmate.front.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.coupon.adaptor.CouponAdaptor;
import shop.chaekmate.front.coupon.dto.request.CouponIssueRequest;
import shop.chaekmate.front.coupon.dto.response.CouponIssueResponse;
import shop.chaekmate.front.coupon.dto.response.IssuedCouponResponse;
import shop.chaekmate.front.coupon.dto.response.UsedCouponResponse;

import java.util.List;

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

    @GetMapping("/coupon-box")
    public String getAvailableCouponsPage(
            @AuthenticationPrincipal CustomPrincipal principal,
            Model model) {
        Long memberId = principal != null ? principal.getMemberId() : null;

        if (memberId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        CommonResponse<List<IssuedCouponResponse>> response =
                couponAdaptor.getAvailableCoupons(memberId);
        List<IssuedCouponResponse> availableCoupons = response.data();

        model.addAttribute("availableCoupons", availableCoupons);
        model.addAttribute("currentPage", "coupon-available");
        return "member/coupons-available";
    }

    @GetMapping("/coupon-history")
    public String getUsedCouponsPage(
            @AuthenticationPrincipal CustomPrincipal principal,
            Model model) {
        Long memberId = principal != null ? principal.getMemberId() : null;

        if (memberId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        CommonResponse<List<UsedCouponResponse>> response =
                couponAdaptor.getUsedCoupons(memberId);
        List<UsedCouponResponse> usedCoupons = response.data();

        model.addAttribute("usedCoupons", usedCoupons);
        model.addAttribute("currentPage", "coupon-history");
        return "member/coupons-used";
    }
}
