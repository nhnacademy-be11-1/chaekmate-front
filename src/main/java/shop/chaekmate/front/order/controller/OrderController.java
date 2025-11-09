package shop.chaekmate.front.order.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/order")
    public String orderPage(Model model) {

        // 주문 번호 생성
        String orderNumber = NanoIdUtils.randomNanoId();
        model.addAttribute("orderNumber", orderNumber);

        // 🧩 1. 주문 상품 더미
        var orderItems = List.of(
                new OrderItem(
                        "이펙티브 자바 3판",
                        "Joshua Bloch",
                        38000,
                        1,
                        38000,
                        "/static/img/cat-1.jpg"
                ),
                new OrderItem(
                        "스프링 인 액션 6판",
                        "Craig Walls",
                        42000,
                        2,
                        84000,
                        "/static/img/cat-2.jpg"
                )
        );
        model.addAttribute("orderItems", orderItems);

        // 🎁 2. 포장지 더미
        var wraps = List.of(
                new Wrap(1L, "기본 포장", 0, "/static/img/wrap_basic.jpg"),
                new Wrap(2L, "선물 포장", 1500, "/img/wrap_gift.jpg")
        );
        model.addAttribute("wraps", wraps);

        // 🏠 3. 배송지 더미 (로그인 사용자)
        var addresses = List.of(
                new Address(1L, "우리집", "서울특별시 강남구 테헤란로 123", "101동 1001호"),
                new Address(2L, "회사", "경기도 성남시 분당구 판교역로 45", "NHN타워 10층")
        );
        model.addAttribute("addresses", addresses);

        // 💳 4. 결제 요약 더미
        var summary = new Summary(
                122000,   // 상품 합계
                5000,     // 쿠폰 할인
                2000,     // 포인트 사용
                1500,     // 포장비
                3000,     // 배송비
                121500    // 총 결제금액
        );
        model.addAttribute("summary", summary);

        // 👤 5. 사용자 더미 (보유 포인트 등)
        var member = new Member("홍길동", "010-1234-5678", "hong@example.com", 12000);
        model.addAttribute("member", member);

        // 🚚 6. 기본 배송일 (오늘 +3일)
        model.addAttribute("defaultDeliveryDate", LocalDate.now().plusDays(3));

        return "order/orderPage";
    }

    // --- 단순 더미 DTO (record 형태) ---
    record OrderItem(String name, String author, int price, int quantity, int subtotal, String thumbnailUrl) {}
    record Wrap(Long id, String name, int price, String imageUrl) {}
    record Address(Long id, String label, String road, String detail) {}
    record Summary(int productsTotal, int couponDiscount, int pointDiscount, int wrapFeeTotal, int shippingFee, int payableTotal) {}
    record Member(String name, String phone, String email, int remainingPoints) {}
}
