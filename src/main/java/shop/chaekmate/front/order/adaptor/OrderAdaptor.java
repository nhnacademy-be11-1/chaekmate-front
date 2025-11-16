package shop.chaekmate.front.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyResponse;

@FeignClient(name = "order-client", url = "${chaekmate.gateway.url}")
public interface OrderAdaptor {

    @GetMapping("/delivery-policy")
    CommonResponse<DeliveryPolicyResponse> getCurrentPolicy();
}