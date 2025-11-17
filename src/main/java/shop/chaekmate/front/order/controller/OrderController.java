package shop.chaekmate.front.order.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.member.adaptor.MemberAdaptor;
import shop.chaekmate.front.member.dto.response.MemberAddressResponse;
import shop.chaekmate.front.order.adaptor.OrderAdaptor;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyResponse;
import shop.chaekmate.front.order.dto.response.WrapperResponse;
import shop.chaekmate.front.point.adaptor.PointHistoryAdaptor;
import shop.chaekmate.front.point.dto.response.PointResponse;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderAdaptor orderAdaptor;
    private final MemberAdaptor memberAdaptor;
    private final PointHistoryAdaptor pointHistoryAdaptor;

    @GetMapping("/order")
    public String orderPage(@AuthenticationPrincipal CustomPrincipal principal, Model model) {
        // orderNumber주문 번호 생성
        String orderNumber = NanoIdUtils.randomNanoId();
        model.addAttribute("orderNumber", orderNumber);

        // 회원식별자
        boolean isLoggedIn = (principal != null);
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            // 실제 회원 정보 조회
            Long memberId = principal.getMemberId();

            PointResponse pointResponse = pointHistoryAdaptor.getMemberPoint(memberId).data();
            var member = new Member("테스트사용자", "01012345678", "test@example.com", pointResponse.point());
            model.addAttribute("member", member);
            // 회원 이름, 전화번호, 이메일 가져오기
//            var memberResponse = orderAdaptor.getMemberInfo(memberId).data();
//            model.addAttribute("member", memberResponse);

            // 실제 회원 주소 목록 조회
            List<MemberAddressResponse> addressResponse = memberAdaptor.getAddresses(memberId).data();
            model.addAttribute("addresses", addressResponse);

        } else {
             // 비회원이면 빈 값 전달
            model.addAttribute("member", null);
            model.addAttribute("addresses", null);
            model.addAttribute("remainingPoints", 0);
        }

        // delivery-policy 현재 배송정책 조회
        DeliveryPolicyResponse policy = orderAdaptor.getCurrentPolicy().data();
        model.addAttribute("deliveryPolicy", policy);


        /// 더미값 주문 상품
        var orderItems = List.of(
                new OrderItem("이펙티브 자바 3판", "Joshua Bloch", 38000, 1, 38000, "/static/img/cat-1.jpg"),
                new OrderItem("스프링 인 액션 6판", "Craig Walls", 42000, 2, 84000, "/static/img/cat-2.jpg")
        );
        model.addAttribute("orderItems", orderItems);
        int productsTotal = orderItems.stream().mapToInt(OrderItem::subtotal).sum();

        // 배송비 계산 (무료배송 기준 반영)
        int shippingFee = (productsTotal >= policy.freeStandardAmount()) ? 0 : policy.deliveryFee();

        // wrapper 포장지 조회
        List<WrapperResponse> wrappers = orderAdaptor.getWrappers().data();
        model.addAttribute("wrappers", wrappers);

        /// 결제 요약
        var summary = new Summary(
                productsTotal,
                0, // 쿠폰 할인
                0, // 포인트 사용
                0, // 포장비
                shippingFee,
                productsTotal + shippingFee
        );
        model.addAttribute("summary", summary);

        // 기본 배송일 (오늘 +3일)
        model.addAttribute("defaultDeliveryDate", LocalDate.now().plusDays(3));

        return "order/orderPage";
    }

    // --- DTO ---
    record OrderItem(String name, String author, int price, int quantity, int subtotal, String thumbnailUrl) {}
    record Member(String name, String phone, String email, int remainingPoints) {}
    record Summary(int productsTotal, int couponDiscount, int pointDiscount, int wrapFeeTotal, int shippingFee, int payableTotal) {}
}