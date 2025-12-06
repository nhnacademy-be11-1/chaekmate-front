package shop.chaekmate.front.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.adaptor.DeliveryPolicyAdaptor;
import shop.chaekmate.front.order.dto.request.DeliveryPolicyRequest;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyHistoryResponse;

@Controller
@RequiredArgsConstructor
public class AdminDeliveryPolicyController {

    private final DeliveryPolicyAdaptor deliveryPolicyAdaptor;

    @GetMapping("/admin/delivery-policies")
    public String listPolicies(
            Model model) {
        CommonResponse<Page<DeliveryPolicyHistoryResponse>> response = deliveryPolicyAdaptor.getDeliveryPolicies();
        model.addAttribute("policies", response.data());
        return "admin/deliverypolicy/delivery-policy-history";
    }

    @PostMapping("/admin/delivery-policy")
    public String createPolicy(@ModelAttribute DeliveryPolicyRequest request) {
        deliveryPolicyAdaptor.createDeliveryPolicy(request);
        return "redirect:/admin/delivery-policy";
    }
}
