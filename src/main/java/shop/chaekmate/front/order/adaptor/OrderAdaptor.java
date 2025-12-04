package shop.chaekmate.front.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.dto.request.OrderSaveRequest;
import shop.chaekmate.front.order.dto.response.OrderHistoryResponse;
import shop.chaekmate.front.order.dto.response.OrderSaveResponse;

@FeignClient(name = "order-client", url = "${chaekmate.gateway.url}")
public interface OrderAdaptor {

    @PostMapping("/orders/save")
    CommonResponse<OrderSaveResponse> saveOrders(@RequestBody OrderSaveRequest request);

    // 비회원 주문내역 조회
    @GetMapping("/orders/history/non-member")
    CommonResponse<Page<OrderHistoryResponse>> getNonMemberOrderHistory(@PageableDefault Pageable pageable,
                                                                        @RequestParam("orderNumber") String orderNumber,
                                                                        @RequestParam("ordererName") String ordererName,
                                                                        @RequestParam("ordererPhone") String ordererPhone);
    // 회원 주문내역 조회
    @GetMapping("/orders/history/member")
    CommonResponse<Page<OrderHistoryResponse>> getMemberOrderHistory(@PageableDefault Pageable pageable);

    @GetMapping("/orders/{orderId}")
    CommonResponse<OrderHistoryResponse> getOrderDetail(@PathVariable Long orderId);

    @GetMapping("/admin/orders")
    CommonResponse<Page<OrderHistoryResponse>> getAllOrders(
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String unitStatus,
            @RequestParam int page,
            @RequestParam int size
    );

    @PostMapping("/admin/ordered-books/{orderedBookId}/shipping")
    CommonResponse<Void> startShipping(@PathVariable Long orderedBookId);
}