package shop.chaekmate.front.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.dto.request.OrderHistoryRequest;
import shop.chaekmate.front.order.dto.response.OrderHistoryResponse;

@FeignClient(name = "order-client", url = "${chaekmate.gateway.url}")
public interface OrderHistoryAdaptor {

    // 비회원 주문내역 조회
    @GetMapping("/orders/history/non-member")
    CommonResponse<Page<OrderHistoryResponse>> getNonMemberOrderHistory(@PageableDefault Pageable pageable,
                                                                        @RequestBody OrderHistoryRequest request);
    // 회원 주문내역 조회
    @GetMapping("/orders/history/member")
    CommonResponse<Page<OrderHistoryResponse>> getMemberOrderHistory(@PageableDefault Pageable pageable);

}
