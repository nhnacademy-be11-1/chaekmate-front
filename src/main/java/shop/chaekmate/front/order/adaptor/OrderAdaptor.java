package shop.chaekmate.front.order.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.dto.request.OrderSaveRequest;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyResponse;
import shop.chaekmate.front.order.dto.response.OrderSaveResponse;
import shop.chaekmate.front.order.dto.response.WrapperResponse;

@FeignClient(name = "order-client", url = "${chaekmate.gateway.url}")
public interface OrderAdaptor {

    @GetMapping("/delivery-policy")
    CommonResponse<DeliveryPolicyResponse> getCurrentPolicy();

    @GetMapping("/wrappers")
    CommonResponse<List<WrapperResponse>> getWrappers();

    @PostMapping("/orders/save")
    CommonResponse<OrderSaveResponse> saveOrders(@RequestBody OrderSaveRequest request);
}