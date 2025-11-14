package shop.chaekmate.front.payment.adaptor;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.payment.dto.request.PaymentApproveRequest;
import shop.chaekmate.front.payment.dto.response.PaymentApproveResponse;

@FeignClient(name = "payment-client", url = "${chaekmate.gateway.url}")
public interface PaymentAdaptor {

    @PostMapping("/payments/approve")
    CommonResponse<PaymentApproveResponse> approve(@Valid @RequestBody PaymentApproveRequest request);
}
