package shop.chaekmate.front.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.dto.request.OrderSaveRequest;
import shop.chaekmate.front.order.dto.response.OrderSaveResponse;

@FeignClient(name = "order-client", url = "${chaekmate.gateway.url}")
public interface OrderAdaptor {

    @PostMapping("/orders/save")
    CommonResponse<OrderSaveResponse> saveOrders(@RequestBody OrderSaveRequest request);
}