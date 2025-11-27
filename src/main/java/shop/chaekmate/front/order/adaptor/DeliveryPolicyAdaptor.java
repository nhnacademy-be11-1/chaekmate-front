package shop.chaekmate.front.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.dto.request.DeliveryPolicyRequest;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyHistoryResponse;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyResponse;

@FeignClient(name = "delivery-policy-client", url = "${chaekmate.gateway.url}")
public interface DeliveryPolicyAdaptor {

    @GetMapping("/delivery-policy")
    CommonResponse<DeliveryPolicyResponse> getCurrentPolicy();

    @GetMapping("/admin/delivery-policy")
    CommonResponse<Page<DeliveryPolicyHistoryResponse>> getDeliveryPolicies();

    @PostMapping("/admin/delivery-policy")
    CommonResponse<DeliveryPolicyResponse> createDeliveryPolicy(@RequestBody DeliveryPolicyRequest request);

}
