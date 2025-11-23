package shop.chaekmate.front.cart.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.chaekmate.front.cart.dto.request.CartItemCreateRequest;
import shop.chaekmate.front.cart.dto.request.CartItemUpdateRequest;
import shop.chaekmate.front.cart.dto.response.CartItemListAdvancedResponse;
import shop.chaekmate.front.cart.dto.response.CartItemListResponse;
import shop.chaekmate.front.cart.dto.response.CartItemUpdateResponse;
import shop.chaekmate.front.cart.service.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private static final String COOKIE_NAME = "Guest-Id";

    // 장바구니 페이지 뷰
    @GetMapping
    public String getCartView(HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {

        this.getOrCreateUuid(request, response);
        CartItemListAdvancedResponse cart = this.cartService.getCart();
        model.addAttribute("cart", cart);
        return "cart/cart";
    }

    // 장바구니 아이템 담기
    @PostMapping("/items")
    @ResponseBody
    public CartItemListResponse addCartItem(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody CartItemCreateRequest cartItemCreateRequest) {
        this.getOrCreateUuid(request, response);
        return this.cartService.addCartItem(cartItemCreateRequest);
    }

    // 장바구니 아이템 수량 변경
    @PutMapping("/items/{bookId}")
    @ResponseBody
    public CartItemUpdateResponse updateCartItem(@PathVariable Long bookId,
                                                 @RequestBody CartItemUpdateRequest request) {
        return this.cartService.updateCartItem(bookId, request);
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/items/{bookId}")
    @ResponseBody
    public void deleteCartItem(@PathVariable Long bookId) {
        this.cartService.deleteCartItem(bookId);
    }

    // 장바구니 비우기
    @DeleteMapping("/flush")
    @ResponseBody
    public void flushCart() {
        this.cartService.flushCart();
    }

    private void getOrCreateUuid(HttpServletRequest request, HttpServletResponse response) {
        boolean hasUuid = false;

        // 1. 요청 쿠키에서 UUID 확인
        if (Objects.nonNull(request.getCookies())) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    hasUuid = true;
                    break;
                }
            }
        }

        // 2. 없으면 새 UUID 생성 및 쿠키에 추가
        if (!hasUuid) {
            String uuid = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, uuid);
            cookie.setPath("/");                    // 모든 경로에서 접근 가능
            cookie.setHttpOnly(true);               // JS 접근 차단
            cookie.setMaxAge(60 * 60 * 24 * 30);    // 30일

            response.addCookie(cookie);
        }
    }
}
