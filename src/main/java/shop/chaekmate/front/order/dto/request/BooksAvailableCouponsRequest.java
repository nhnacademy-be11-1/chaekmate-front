package shop.chaekmate.front.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BooksAvailableCouponsRequest(
        @NotEmpty(message = "책 목록은 비어있을 수 없습니다.")
        @Valid List<BookCouponCheckRequest> books
) {}
