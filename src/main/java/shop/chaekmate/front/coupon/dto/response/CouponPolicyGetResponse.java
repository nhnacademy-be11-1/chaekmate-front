package shop.chaekmate.front.coupon.dto.response;

import java.time.LocalDateTime;

/**
 * 쿠폰 정책 상세 조회 응답
 * 백엔드의 CouponPolicyGetResponse와 동일
 */
public record CouponPolicyGetResponse(
        long couponPolicyId,
        String name,
        String type,                      // "도서" (한글명)
        String couponAppliedTargetNames,  // "책1, 책2, 책3" (쉼표로 구분된 문자열)
        String couponAppliedPeriodType,   // "30일" (한글명)
        LocalDateTime appliedStartedAt,
        LocalDateTime appliedExpiredAt,
        String discountType,              // "정액" (한글명)
        int discountValue,
        int minAvailableAmount,
        long maxAppliedAmount,
        Long remainingQuantity            // nullable
) {
    /**
     * 할인 표시 문자열
     */
    public String discountDisplay() {
        return discountType.equals("정액")
                ? String.format("%,d원", discountValue)
                : String.format("%d%%", discountValue);
    }
}
