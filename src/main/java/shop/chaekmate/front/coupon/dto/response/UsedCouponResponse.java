package shop.chaekmate.front.coupon.dto.response;

import java.time.LocalDateTime;

public record UsedCouponResponse(
        String couponName,
        String discountDescription,
        LocalDateTime usedAt
) {
}
