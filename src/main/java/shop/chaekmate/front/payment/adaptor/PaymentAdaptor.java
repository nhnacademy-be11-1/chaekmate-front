package shop.chaekmate.front.payment.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.payment.dto.request.PaymentApproveRequest;
import shop.chaekmate.front.payment.dto.request.PaymentCancelRequest;
import shop.chaekmate.front.payment.dto.response.PaymentApproveResponse;
import shop.chaekmate.front.payment.dto.response.PaymentCancelResponse;

@FeignClient(name = "payment-client", url = "${chaekmate.gateway.url}")
public interface PaymentAdaptor {

    @PostMapping("/payments/approve")
    CommonResponse<?> approve(@RequestBody PaymentApproveRequest request);

    @PostMapping("/payments/cancel")
    CommonResponse<PaymentCancelResponse> cancel(@RequestBody PaymentCancelRequest request);
}
