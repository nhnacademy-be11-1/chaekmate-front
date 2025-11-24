package shop.chaekmate.front.coupon.dto.response;

import java.time.LocalDateTime;

/**
 * 쿠폰 정책 목록 조회 응답
 * 백엔드의 CouponPoliciesGetResponse와 동일
 */
public record CouponPoliciesGetResponse(
        long couponPolicyId,
        String name,
        String discountType,      // "정액" or "정률" (한글명)
        int discountValue,
        LocalDateTime appliedStartedAt,
        LocalDateTime appliedExpiredAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
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
