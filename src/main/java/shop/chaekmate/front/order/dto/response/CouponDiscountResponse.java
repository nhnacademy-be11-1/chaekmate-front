package shop.chaekmate.front.order.dto.response;

public record CouponDiscountResponse(
        Long couponId,
        String name,
        Integer rate,        // RATE 쿠폰 % (아니면 null)
        Integer amount       // AMOUNT 쿠폰 금액 (아니면 null)
) {}
