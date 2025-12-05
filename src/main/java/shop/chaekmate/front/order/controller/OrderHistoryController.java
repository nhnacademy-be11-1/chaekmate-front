package shop.chaekmate.front.order.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.chaekmate.front.auth.dto.response.PaycoAuthorizationResponse;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.auth.service.AuthService;
import shop.chaekmate.front.order.dto.request.OrderHistoryRequest;
import shop.chaekmate.front.order.dto.response.OrderHistoryResponse;
import shop.chaekmate.front.order.dto.response.OrderedBookHistoryResponse;
import shop.chaekmate.front.order.service.OrderHistoryService;
import shop.chaekmate.front.review.service.ReviewService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;
    private final ReviewService reviewService;
    private final AuthService authService;

    // 주문 내역 클릭 시
    // 회원 -> 주문내역 리다이렉트 반환
    // 비회원 -> 로그인, 비회원으로 주문 조회 뷰 반환
    @GetMapping("/orders/history")
    public String getOrderHistory(@AuthenticationPrincipal CustomPrincipal principal, Model model){

        if(Objects.nonNull(principal) && Objects.nonNull(principal.getMemberId())){
            // 회원
            log.debug("회원 주문 조회 진입 {}",principal.getName());
            return "redirect:/orders/history/list";
        }

        // PAYCO 인증 URL 가져오기
        try {
            ResponseEntity<PaycoAuthorizationResponse> paycoResponse = authService.getPaycoAuthorizationUrl();
            if (paycoResponse.getStatusCode().is2xxSuccessful() && paycoResponse.getBody() != null) {
                model.addAttribute("paycoAuthorizationUrl", paycoResponse.getBody().authorizationUrl());
            }
        } catch (Exception e) {
            // PAYCO URL 가져오기 실패 시 무시 (일반 로그인만 가능)
            log.warn("PAYCO 인증 URL 가져오기 실패", e);
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

        // 리뷰 작성 여부 확인 (DELIVERED 상태인 orderedBook만)
        Map<Long, Boolean> reviewExistsMap = new HashMap<>();
        if (!pagedResponse.isEmpty()) {
            Map<Long, Long> orderedBookIdToBookIdMap = new HashMap<>();

            for (OrderHistoryResponse order : pagedResponse.getContent()) {
                for (OrderedBookHistoryResponse book : order.getOrderedBooks()) {
                    if ("DELIVERED".equals(book.getUnitStatus())) {
                        orderedBookIdToBookIdMap.put(book.getOrderedBookId(), book.getBookId());
                    }
                }
            }

            if (!orderedBookIdToBookIdMap.isEmpty()) {
                reviewExistsMap = reviewService.checkReviewExistsByOrderedBookIds(orderedBookIdToBookIdMap);
            }
        }

        model.addAttribute("pagedResponse", pagedResponse);
        model.addAttribute("reviewExistsMap", reviewExistsMap);

        return "order/history/order-history-list";
    }
}
