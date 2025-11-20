package shop.chaekmate.front.coupon.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType {
    RATE("정률"),
    AMOUNT("정액");

    private final String displayName;
}
