package shop.chaekmate.front.order.dto.response;

import lombok.Data;

@Data
public class OrderedBookHistoryResponse {
    private Long orderedBookId;
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
    private Integer finalUnitPrice;
    private Long orderId;
    private Integer originalPrice;
    private Integer salesPrice;
    private Integer discountPrice;
    private Long wrapperId;
    private Integer wrapperPrice;
    private Long issuedCouponId;
    private Integer couponDiscount;
    private Integer pointUsed;
    private String unitStatus;
    private Long totalPrice;
}
