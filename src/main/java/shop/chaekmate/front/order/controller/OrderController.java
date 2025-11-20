package shop.chaekmate.front.order.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.book.adaptor.BookAdaptor;
import shop.chaekmate.front.book.adaptor.BookImageAdaptor;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.member.adaptor.MemberAdaptor;
import shop.chaekmate.front.member.dto.response.MemberAddressResponse;
import shop.chaekmate.front.order.adaptor.OrderAdaptor;
import shop.chaekmate.front.order.dto.request.OrderItem;
import shop.chaekmate.front.order.dto.request.OrderSaveRequest;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyResponse;
import shop.chaekmate.front.order.dto.response.OrderSaveResponse;
import shop.chaekmate.front.order.dto.response.WrapperResponse;
import shop.chaekmate.front.point.adaptor.PointHistoryAdaptor;
import shop.chaekmate.front.point.dto.response.PointResponse;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderAdaptor orderAdaptor;
    private final MemberAdaptor memberAdaptor;
    private final BookAdaptor bookAdaptor;
    private final BookImageAdaptor bookImageAdaptor;
    private final PointHistoryAdaptor pointHistoryAdaptor;

    @GetMapping("/orders")
    public String orderPage(@AuthenticationPrincipal CustomPrincipal principal,
//                            @RequestBody OrderRequest orderRequest,
                            @RequestParam(required = false) Long bookId,
                            @RequestParam(required = false) Integer quantity,
                            Model model) {

        List<OrderItem> orderItems = List.of();

        if (bookId != null && quantity != null) {
            BookDetailResponse book = bookAdaptor.getBookById(bookId).data();
            orderItems = List.of(getOrderItem(book, quantity));
        }
        model.addAttribute("orderItems", orderItems);

        // 회원식별자
        boolean isLoggedIn = (principal != null);
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            // 실제 회원 정보 조회
            Long memberId = principal.getMemberId();

            PointResponse pointResponse = pointHistoryAdaptor.getMemberPoint(memberId).data();
            var member = new Member("테스트사용자", "01012345678", "test@example.com", pointResponse.point());
            model.addAttribute("member", member);

            // todo 실제 회원 이름, 전화번호, 이메일 가져오기 (member 로직 구현 x)
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

        int productsTotal = orderItems.stream().mapToInt(OrderItem::subtotal).sum();

        // 배송비 계산 (무료배송 기준 반영)
        int shippingFee = (productsTotal >= policy.freeStandardAmount()) ? 0 : policy.deliveryFee();

        // wrapper 포장지 조회
        List<WrapperResponse> wrappers = orderAdaptor.getWrappers().data();
        model.addAttribute("wrappers", wrappers);

        // 결제 요약 수정
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

    @PostMapping("/orders/save")
    @ResponseBody
    public OrderSaveResponse saveOrder(@RequestBody OrderSaveRequest request) {
        return orderAdaptor.saveOrders(request).data();
    }


    private OrderItem getOrderItem(BookDetailResponse book, Integer quantity) {
        BookThumbnailResponse thumbnail = bookImageAdaptor.getBookThumbnail(book.id()).data();

        String thumbnailUrl = Boolean.TRUE.equals(thumbnail.isThumbnail()) ? thumbnail.imageUrl() : null;

        int originalPrice = book.price();
        int salesPrice = book.salesPrice();
        int discountAmount = originalPrice - salesPrice;
        int discountRate = Math.round((float) discountAmount / originalPrice * 100);

        return new OrderItem(
                book.id(),
                book.title(),
                book.author(),
                book.publisher(),
                originalPrice,
                salesPrice,
                discountRate,
                discountAmount,
                quantity,
                salesPrice * quantity,
                thumbnailUrl
        );
    }

    // --- DTO ---
    record Member(String name, String phone, String email, int remainingPoints) {
    }

    record Summary(int productsTotal, int couponDiscount, int pointDiscount, int wrapFeeTotal, int shippingFee,
                   int payableTotal) {
    }
}