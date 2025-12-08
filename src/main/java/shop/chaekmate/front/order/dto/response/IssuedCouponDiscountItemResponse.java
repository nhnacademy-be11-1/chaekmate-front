package shop.chaekmate.front.order.dto.response;

import shop.chaekmate.front.coupon.type.DiscountType;

public record IssuedCouponDiscountItemResponse(
        Long couponId,
        String name,
        DiscountType discountType,
        Integer rate,
        Integer amount,
        int discountAmount
) {}
