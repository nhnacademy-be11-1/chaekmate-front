package shop.chaekmate.front.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.payment.adaptor.PaymentAdaptor;
import shop.chaekmate.front.payment.dto.request.PaymentApproveRequest;
import shop.chaekmate.front.payment.dto.request.PaymentCallbackRequest;
import shop.chaekmate.front.payment.dto.response.PaymentApproveResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentAdaptor paymentAdaptor;

    //주문서 -> PaymentReadyRequest
    @PostMapping("/point")
    public String point(@ModelAttribute PaymentCallbackRequest request, Model model) {

        CommonResponse<PaymentApproveResponse> approveResponse =
                paymentAdaptor.approve(new PaymentApproveRequest(
                        "POINT",
                        null,
                        request.orderId(),
                        0L,
                        request.pointUsed()
                ));

        model.addAttribute("approveResponse", approveResponse.data());
        return "payment/payment-success";
    }


    @GetMapping("/success")
    public String success(@ModelAttribute PaymentCallbackRequest request, Model model) {
        try {
            CommonResponse<PaymentApproveResponse> approveResponse = paymentAdaptor.approve(
                    new PaymentApproveRequest("TOSS", request.paymentKey(), request.orderId(), request.amount(), request.pointUsed())
            );

            model.addAttribute("approveResponse", approveResponse.data());
            log.info("[결제 성공] orderId={}, status={}, totalAmount={}, approvedAt={}",
                    approveResponse.data().orderId(), approveResponse.data().status(), approveResponse.data().totalAmount(), approveResponse.data().approvedAt());

            return "payment/payment-success";

        } catch (Exception e) {
            log.error("[결제 승인 실패] 주문번호={}, 사유={}", request.orderId(), e.getMessage());
            model.addAttribute("code", "SERVER-500");
            model.addAttribute("message", "결제 승인 처리 중 오류가 발생했습니다!");
            return "payment/payment-fail";
        }
    }

    @GetMapping("/fail")
    public String failToss(@ModelAttribute PaymentCallbackRequest request, Model model) {

        log.warn("[TOSS 결제 실패 콜백] orderId={}, code={}, message={}",
                request.orderId(), request.code(), request.message());

        model.addAttribute("code", request.code());
        model.addAttribute("message", request.message());

        return "payment/payment-fail";
    }
}
