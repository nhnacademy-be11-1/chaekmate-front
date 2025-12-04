package shop.chaekmate.front.coupon.dto.response;

import java.time.LocalDateTime;

public record BookCouponPolicyResponse(
        Long id,
        String name,
        String type,
        String appliedPeriodType,
        LocalDateTime appliedStartedAt,
        LocalDateTime appliedExpiredAt,
        String discountType,
        Integer discountValue,
        Integer minAvailableAmount,
        Long maxAppliedAmount,
        boolean isIssued
) {
}
