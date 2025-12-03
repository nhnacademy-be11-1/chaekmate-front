package shop.chaekmate.front.coupon.dto.response;

import java.time.LocalDateTime;

public record IssuedCouponResponse(
        Long couponId,
        String couponName,
        String discountDescription,
        long minAvailableAmount,
        long maxAppliedAmount,
        LocalDateTime expiredAt,
        long daysUntilExpired
) {
}
