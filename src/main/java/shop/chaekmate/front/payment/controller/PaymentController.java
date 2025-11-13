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
import shop.chaekmate.front.payment.dto.request.PaymentReadyRequest;
import shop.chaekmate.front.payment.dto.response.PaymentApproveResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentAdaptor paymentAdaptor;

    //주문서
    @PostMapping
    public String redirectToPaymentPage(@ModelAttribute PaymentReadyRequest request, Model model) {
        log.info("[결제 요청] 주문번호={}, 결제금액={}",
                request.orderNumber(), request.price());

        if (request.price() == 0L) {
            log.info("[포인트 결제 감지] Toss 페이지 생략 → 바로 승인 진행");

            var approveRequest = new PaymentApproveRequest(
                    "POINT",           // 결제수단
                    null,              // Toss paymentKey 없음
                    request.orderNumber(),
                    0L,                // 결제금액 없음
                    request.pointUsed() // 사용된 포인트 금액
            );

            try {
                CommonResponse<PaymentApproveResponse> response = paymentAdaptor.approve(approveRequest);
                model.addAttribute("approveResponse", response.data());
                log.info("[포인트 결제 완료] orderNumber={}, totalAmount={}, approvedAt={}",
                        response.data().orderId(), response.data().totalAmount(), response.data().approvedAt());

                return "payment/payment-success";
            } catch (Exception e) {
                log.error("[포인트 결제 실패] 주문번호={}, 사유={}", request.orderNumber(), e.getMessage());
                model.addAttribute("code", "POINT-500");
                model.addAttribute("message", "포인트 결제 중 오류가 발생했습니다.");
                return "payment/payment-fail";
            }
        }

        model.addAttribute("orderNumber", request.orderNumber());
        model.addAttribute("orderName", request.orderName());
        model.addAttribute("price", request.price());
        model.addAttribute("pointUsed",request.pointUsed());

        return "payment/payment";
    }

    @GetMapping("/success")
    public String success(@ModelAttribute PaymentCallbackRequest request, Model model) {
        try {
            long amount = Long.parseLong(request.amount());
            int pointUsed = request.pointUsed()!=null?Integer.parseInt(request.pointUsed()):0;

            CommonResponse<PaymentApproveResponse> approveResponse = paymentAdaptor.approve(
                    new PaymentApproveRequest("TOSS", request.paymentKey(), request.orderId(), amount,pointUsed)
            );

            model.addAttribute("approveResponse", approveResponse.data());
            log.info("[결제 성공] orderId={}, status={}, totalAmount={}, approvedAt={}",
                    approveResponse.data().orderId(), approveResponse.data().status(), approveResponse.data().totalAmount(), approveResponse.data().approvedAt());

            return "payment/payment-success";

        } catch (Exception e) {
            log.error("[결제 승인 실패] 주문번호={}, 사유={}", request.orderId(), e.getMessage());
            model.addAttribute("code", "SERVER-500");
            model.addAttribute("message", "결제 승인 처리 중 오류가 발생했습니다.");
            return "payment/payment-fail";
        }
    }

    @GetMapping("/fail")
    public String fail(@RequestParam String orderId, @RequestParam String code,
                       @RequestParam String message, Model model) {
        log.warn("[결제 실패 콜백] orderId={}, code={}, message={}", orderId, code, message);
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "payment/payment-fail";
    }
}
