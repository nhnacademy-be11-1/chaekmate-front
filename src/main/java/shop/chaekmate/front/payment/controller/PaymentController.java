package shop.chaekmate.front.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.payment.adaptor.PaymentAdaptor;
import shop.chaekmate.front.payment.dto.request.PaymentApproveRequest;
import shop.chaekmate.front.payment.dto.response.PaymentApproveResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentAdaptor paymentAdaptor;

    @GetMapping("/pay")
    public String showPaymentPage(@RequestParam String orderNumber,
                                  @RequestParam int price,
                                  @RequestParam String orderName,
                                  Model model) {

        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("price", price);
        model.addAttribute("orderName", orderName);
        return "payment";
    }

    @GetMapping("/pay/success")
    public String handlePaymentSuccess(@RequestParam String paymentKey,
                                       @RequestParam String orderId,
                                       @RequestParam long amount,
                                       Model model) {
        log.info("paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);

        CommonResponse<PaymentApproveResponse> response = paymentAdaptor.approve(new PaymentApproveRequest("TOSS", paymentKey, orderId, amount));
        PaymentApproveResponse data = response.data();

        model.addAttribute("response", data);

        try {
            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
            String responseJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            model.addAttribute("responseJson", responseJson);
        } catch (Exception e) {
            log.warn("JSON 변환 실패", e);
        }
        return "payment-success";
    }

    @GetMapping("/pay/fail")
    public String handlePaymentFail(@RequestParam String code,
                                    @RequestParam String message,
                                    Model model) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "payment-fail";
    }
}
