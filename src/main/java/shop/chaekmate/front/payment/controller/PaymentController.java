package shop.chaekmate.front.payment.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.payment.adaptor.PaymentAdaptor;
import shop.chaekmate.front.payment.dto.request.PaymentApproveRequest;
import shop.chaekmate.front.payment.dto.request.PaymentCallbackRequest;
import shop.chaekmate.front.payment.dto.request.PaymentCancelRequest;
import shop.chaekmate.front.payment.dto.request.PointPaymentRequest;
import shop.chaekmate.front.payment.dto.request.ReturnBooksRequest;
import shop.chaekmate.front.payment.dto.response.PaymentAbortedResponse;
import shop.chaekmate.front.payment.dto.response.PaymentApproveResponse;
import shop.chaekmate.front.payment.dto.response.ReturnBooksResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentAdaptor paymentAdaptor;

    @PostMapping("/payments/point")
    public String point(@ModelAttribute PointPaymentRequest request, Model model) {

        CommonResponse<?> response =
                paymentAdaptor.approve(new PaymentApproveRequest(
                        "POINT",
                        null,
                        request.orderId(),
                        0L,
                        request.pointUsed()
                ));
        Object data = response.data();

        // 응답이 LinkedHashMap 형태일 때 처리
        if (data instanceof LinkedHashMap<?, ?> map) {

            // 실패
            if (map.containsKey("code") && map.containsKey("message") && !map.containsKey("orderId")) {

                PaymentAbortedResponse aborted = new PaymentAbortedResponse(
                        map.get("code").toString(),
                        map.get("message").toString(),
                        LocalDateTime.now()   // POINT는 approvedAt이 없으므로 현재시간 사용
                );

                model.addAttribute("code", aborted.code());
                model.addAttribute("message", aborted.message());
                return "payment/payment-fail";
            }

            // 성공
            PaymentApproveResponse approve = new PaymentApproveResponse(
                    map.get("orderId").toString(),
                    Long.parseLong(map.get("totalAmount").toString()),
                    Integer.parseInt(map.get("pointUsed").toString()),
                    map.get("status").toString(),
                    LocalDateTime.parse(map.get("approvedAt").toString())
            );

            model.addAttribute("approveResponse", approve);
            return "payment/payment-success";
        }

        // data가 LinkedHashMap이 아닌 경우 실패
        return "payment/payment-fail";
    }


    @GetMapping("/payments/success")
    public String success(@ModelAttribute PaymentCallbackRequest request, Model model) {
        CommonResponse<?> response = paymentAdaptor.approve(
                new PaymentApproveRequest("TOSS", request.paymentKey(),
                        request.orderId(), request.amount(), request.pointUsed())
        );

        Object data = response.data();

        if (data instanceof LinkedHashMap<?, ?> map) {

            // 실패: code + message 존재
            if (map.containsKey("code") && map.containsKey("message") && !map.containsKey("orderId")) {
                PaymentAbortedResponse aborted = new PaymentAbortedResponse(
                        map.get("code").toString(),
                        map.get("message").toString(),
                        LocalDateTime.parse(map.get("approvedAt").toString())
                );

                model.addAttribute("code", aborted.code());
                model.addAttribute("message", aborted.message());
                return "payment/payment-fail";
            }

            // 성공: orderId, status, totalAmount 존재
            PaymentApproveResponse approve = new PaymentApproveResponse(
                    map.get("orderId").toString(),
                    Long.parseLong(map.get("totalAmount").toString()),
                    Integer.parseInt(map.get("pointUsed").toString()),
                    map.get("status").toString(),
                    LocalDateTime.parse(map.get("approvedAt").toString())
            );

            model.addAttribute("approveResponse", approve);

            log.info("[결제 성공] orderId={}, status={}, totalAmount={}, approvedAt={}",
                    approve.orderId(), approve.status(), approve.totalAmount(), approve.approvedAt());

            return "payment/payment-success";
        }
        return "payment/payment-fail";
    }

    @GetMapping("/payments/fail")
    public String failToss(@ModelAttribute PaymentCallbackRequest request, Model model) {

        log.warn("[TOSS 결제 실패 콜백] orderId={}, code={}, message={}",
                request.orderId(), request.code(), request.message());

        model.addAttribute("code", request.code());
        model.addAttribute("message", request.message());

        return "payment/payment-fail";
    }

    @PostMapping("/payments/cancel")
    public ResponseEntity<Void> cancel(@RequestBody PaymentCancelRequest request) {
        paymentAdaptor.cancel(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payments/return-request")
    public ResponseEntity<CommonResponse<ReturnBooksResponse>> returnRequest(@RequestBody ReturnBooksRequest request) {
        CommonResponse<ReturnBooksResponse> response = paymentAdaptor.returnRequest(request);
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @PostMapping("/admin/payments/return-approve")
    public CommonResponse<ReturnBooksResponse> approveRefund(@RequestBody ReturnBooksRequest request) {
        return paymentAdaptor.returnApprove(request);
    }
}
