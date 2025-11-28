package shop.chaekmate.front.coupon.dto.response;

import java.time.LocalDateTime;

public record CouponIssueResponse(
        Long issuedCouponId,
        Long couponPolicyId,
        String couponName,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt
) {
}
