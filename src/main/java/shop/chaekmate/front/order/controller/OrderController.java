package shop.chaekmate.front.order.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.order.adaptor.OrderAdaptor;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyResponse;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderAdaptor orderAdaptor;

    @GetMapping("/order")
    public String orderPage(Model model) {

        // 주문 번호 생성
        String orderNumber = NanoIdUtils.randomNanoId();
        model.addAttribute("orderNumber", orderNumber);

        // 🧩 1. 주문 상품
        var orderItems = List.of(
                new OrderItem("이펙티브 자바 3판", "Joshua Bloch", 38000, 1, 38000, "/static/img/cat-1.jpg"),
                new OrderItem("스프링 인 액션 6판", "Craig Walls", 42000, 2, 84000, "/static/img/cat-2.jpg")
        );
        model.addAttribute("orderItems", orderItems);

        int productsTotal = orderItems.stream().mapToInt(OrderItem::subtotal).sum();

        // 🏷️ 2. core 서버에서 현재 배송정책 조회
        var response = orderAdaptor.getCurrentPolicy();
        DeliveryPolicyResponse policy = response.data();

        // 🚚 3. 배송비 계산 (무료배송 기준 반영)
        int shippingFee = (productsTotal >= policy.freeStandardAmount()) ? 0 : policy.deliveryFee();

        // 💳 4. 결제 요약
        var summary = new Summary(
                productsTotal,
                0, // 쿠폰 할인
                0, // 포인트 사용
                0, // 포장비
                shippingFee,
                productsTotal + shippingFee
        );
        model.addAttribute("summary", summary);
        model.addAttribute("deliveryPolicy", policy);

        // 👤 5. 사용자 더미
        var member = new Member("홍길동", "010-1234-5678", "hong@example.com", 18000);
        model.addAttribute("member", member);

        // 🏠 6. 배송지 더미
        var addresses = List.of(
                new Address(1L, "우리집", "서울특별시 강남구 테헤란로 123", "101동 1001호", "06234"),
                new Address(2L, "회사", "경기도 성남시 분당구 판교역로 45", "NHN타워 10층", "13487")
        );
        model.addAttribute("addresses", addresses);

        // 🚀 7. 기본 배송일 (오늘 +3일)
        model.addAttribute("defaultDeliveryDate", LocalDate.now().plusDays(3));

        return "order/orderTest";
    }

    // --- DTO ---
    record OrderItem(String name, String author, int price, int quantity, int subtotal, String thumbnailUrl) {}
    record Address(Long id, String memo, String streetName, String detail, String zipcode) {}
    record Member(String name, String phone, String email, int remainingPoints) {}
    record Summary(int productsTotal, int couponDiscount, int pointDiscount, int wrapFeeTotal, int shippingFee, int payableTotal) {}
}