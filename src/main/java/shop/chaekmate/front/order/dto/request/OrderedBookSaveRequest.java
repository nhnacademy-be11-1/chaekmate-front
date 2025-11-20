package shop.chaekmate.front.order.dto.request;

public record OrderedBookSaveRequest(
        Long bookId,
        Integer quantity,
        Integer originalPrice,
        Integer salesPrice,
        Integer discountPrice,

        Long wrapperId,
        Integer wrapperPrice,

        Long issuedCouponId,
        Integer couponDiscount,

        Integer pointUsed,

        Integer finalUnitPrice
) {}
