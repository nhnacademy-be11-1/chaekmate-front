package shop.chaekmate.front.coupon.dto.request;

import shop.chaekmate.front.coupon.type.CouponAppliedPeriodType;
import shop.chaekmate.front.coupon.type.CouponType;
import shop.chaekmate.front.coupon.type.DiscountType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 쿠폰 정책 수정 요청
 * 백엔드의 CouponPolicyUpdateRequest와 동일
 */
public record CouponPolicyUpdateRequest(
        String name,
        CouponType type,
        CouponAppliedPeriodType appliedPeriodType,
        List<Long> ids,
        LocalDateTime appliedStartedAt,
        LocalDateTime appliedExpiredAt,
        DiscountType discountType,
        int discountValue,
        Integer minAvailableAmount,
        Long maxAppliedAmount,
        Long remainingQuantity
) {}
