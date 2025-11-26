package shop.chaekmate.front.order.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.order.dto.request.OrderHistoryRequest;
import shop.chaekmate.front.order.dto.response.OrderHistoryResponse;
import shop.chaekmate.front.order.service.OrderHistoryService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    // 주문 내역 클릭 시
    // 회원 -> 주문내역 리다이렉트 반환
    // 비회원 -> 로그인, 비회원으로 주문 조회 뷰 반환
    @GetMapping("/orders/history")
    public String getOrderHistory(@AuthenticationPrincipal CustomPrincipal principal){

        if(Objects.nonNull(principal) && Objects.nonNull(principal.getMemberId())){
            // 회원
            log.debug("회원 주문 조회 진입 {}",principal.getName());
            return "redirect:/orders/history/list";
        }

        // 비회원
        return "order/history/order-history-login";
    }

    // 주문 내역 리스트 뷰
    @GetMapping("/orders/history/list")
    public String getOrderHistoryList(@ModelAttribute OrderHistoryRequest orderHistoryRequest,
                                      @AuthenticationPrincipal CustomPrincipal principal,
                                      @PageableDefault(size=3) Pageable pageable,
                                      Model model){

        Page<OrderHistoryResponse> pagedResponse = Page.empty();

        // 비회원 모델 어트리뷰트 추가
        if(Objects.nonNull(orderHistoryRequest) && Objects.isNull(principal)){
            pagedResponse = orderHistoryService.getNonMemberOrderHistory(pageable, orderHistoryRequest);
        }

        // 회원 모델 어트리뷰트 추가
        if(Objects.nonNull(principal)){
            pagedResponse = orderHistoryService.getMemberOrderHistory(pageable);
        }

        model.addAttribute("pagedResponse", pagedResponse);

        return "order/history/order-history-list";
    }
}
