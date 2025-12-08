package shop.chaekmate.front.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.adaptor.OrderAdaptor;
import shop.chaekmate.front.order.dto.response.OrderHistoryResponse;
import shop.chaekmate.front.order.type.OrderStatusType;
import shop.chaekmate.front.order.type.OrderedBookStatusType;

@Controller
@RequiredArgsConstructor
public class AdminOrderedBookController {
    private final OrderAdaptor orderAdaptor;

    @GetMapping("/admin/all-orders")
    public String adminAllOrdersPage(Model model) {
        model.addAttribute("orderStatusList", OrderStatusType.values());
        model.addAttribute("unitStatusList", OrderedBookStatusType.values());
        return "admin/order/admin-order-list";
    }

    @ResponseBody
    @GetMapping("/admin/orders")
    public CommonResponse<Page<OrderHistoryResponse>> getAllOrders(
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String unitStatus,
            Pageable pageable) {

        return orderAdaptor.getAllOrders(
                orderStatus,
                unitStatus,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @ResponseBody
    @GetMapping("/orders/{orderId}")
    public CommonResponse<OrderHistoryResponse> getDetail(@PathVariable Long orderId) {
        return orderAdaptor.getOrderDetail(orderId);
    }

    @PostMapping("/admin/ordered-books/{orderedBookId}/shipping")
    @ResponseBody
    public CommonResponse<Void> startShipping(@PathVariable Long orderedBookId) {
        return orderAdaptor.startShipping(orderedBookId);
    }
}
