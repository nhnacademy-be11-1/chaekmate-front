package shop.chaekmate.front.coupon.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponAppliedPeriodType {
    THIRTY_DAYS("30일"),
    MONTH_LAST_DAY("월말일"),
    CUSTOM_PERIOD("특정기간");

    private final String displayName;
}
