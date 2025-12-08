package shop.chaekmate.front.order.dto.response;

import java.util.List;
import java.util.Map;

public record BooksAvailableCouponsDetailResponse(
        List<CouponDiscountResponse> coupons,
        Map<Long, List<IssuedCouponDiscountItemResponse>> bookCouponMap
) {}
