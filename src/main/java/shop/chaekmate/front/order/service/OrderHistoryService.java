package shop.chaekmate.front.order.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.order.adaptor.OrderHistoryAdaptor;
import shop.chaekmate.front.order.dto.request.OrderHistoryRequest;
import shop.chaekmate.front.order.dto.response.OrderHistoryResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderHistoryAdaptor orderHistoryAdaptor;

    public Page<OrderHistoryResponse> getNonMemberOrderHistory(Pageable pageable, OrderHistoryRequest orderHistoryRequest) {
        Page<OrderHistoryResponse> response;

        try {
            response = orderHistoryAdaptor.getNonMemberOrderHistory(
                pageable,
                orderHistoryRequest.getOrderNumber(),
                orderHistoryRequest.getOrdererName(),
                orderHistoryRequest.getOrdererPhone()
            ).data();
        } catch (FeignException e) {
            log.error("주문 내역 조회 중 에러 발생 - status: {}, message: {}", e.status(), e.getMessage());
            return Page.empty();
        }

        return response;
    }

    public Page<OrderHistoryResponse> getMemberOrderHistory(Pageable pageable) {
        Page<OrderHistoryResponse> response;

        try {
            response = orderHistoryAdaptor.getMemberOrderHistory(pageable).data();
        } catch (FeignException e) {
            log.error("주문 내역 조회 중 에러 발생 - status: {}, message: {}", e.status(), e.getMessage());
            return Page.empty();
        }

        return response;
    }
}
