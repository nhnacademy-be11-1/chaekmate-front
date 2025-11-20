package shop.chaekmate.front.coupon.dto.request;

import shop.chaekmate.front.coupon.type.CouponAppliedPeriodType;
import shop.chaekmate.front.coupon.type.CouponType;
import shop.chaekmate.front.coupon.type.DiscountType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 쿠폰 정책 생성 요청
 * 백엔드의 CouponPolicyCreateRequest와 동일
 */
public record CouponPolicyCreateRequest(
        String name,                             // 필수
        CouponType type,                         // 필수 (Enum)
        CouponAppliedPeriodType appliedPeriodType,  // 필수 (Enum)
        List<Long> ids,                          // 선택
        LocalDateTime appliedStartedAt,          // 선택
        LocalDateTime appliedExpiredAt,          // 선택
        DiscountType discountType,               // 필수 (Enum)
        int discountValue,                       // 필수
        Integer minAvailableAmount,              // 필수
        Long maxAppliedAmount,                   // 선택
        Long remainingQuantity                   // 선택
) {}
