package shop.chaekmate.front.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderItemsRequest(
        @NotEmpty(message = "상품 정보는 최소 1개 이상이어야 합니다.")
        List<OrderItemRequest> items
) {}
