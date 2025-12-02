package shop.chaekmate.front.cart.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.chaekmate.front.cart.dto.request.CartItemCreateRequest;
import shop.chaekmate.front.cart.dto.request.CartItemUpdateRequest;
import shop.chaekmate.front.cart.dto.response.CartItemListAdvancedResponse;
import shop.chaekmate.front.cart.dto.response.CartItemListResponse;
import shop.chaekmate.front.cart.dto.response.CartItemUpdateResponse;
import shop.chaekmate.front.cart.service.CartService;
import shop.chaekmate.front.cart.util.GuestIdResolver;
import shop.chaekmate.front.order.adaptor.DeliveryPolicyAdaptor;
import shop.chaekmate.front.order.dto.response.DeliveryPolicyResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final GuestIdResolver guestIdResolver;
    private final DeliveryPolicyAdaptor deliveryPolicyAdaptor;

    // 장바구니 페이지 뷰
    @GetMapping
    public String getCartView(HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {

        String guestId = this.guestIdResolver.getOrCreateUuid(request, response);

        CartItemListAdvancedResponse cart = this.cartService.getCart(guestId);
        DeliveryPolicyResponse currentDelivery = this.deliveryPolicyAdaptor.getCurrentPolicy().data();

        model.addAttribute("cart", cart);
        model.addAttribute("currentDelivery", currentDelivery);

        return "cart/cart";
    }

    // 장바구니 아이템 담기
    @PostMapping("/items")
    @ResponseBody
    public CartItemListResponse addCartItem(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody CartItemCreateRequest cartItemCreateRequest) {

        String guestId = this.guestIdResolver.getOrCreateUuid(request, response);
        return this.cartService.addCartItem(cartItemCreateRequest, guestId);
    }

    // 장바구니 아이템 수량 변경
    @PostMapping("/items/{bookId}")
    @ResponseBody
    public CartItemUpdateResponse updateCartItem(@PathVariable Long bookId,
                                                 @RequestBody CartItemUpdateRequest request) {
        return this.cartService.updateCartItem(bookId, request);
    }

    // 장바구니 아이템 삭제
    @PostMapping("/items/delete/{bookId}")
    @ResponseBody
    public void deleteCartItem(@PathVariable Long bookId) {
        this.cartService.deleteCartItem(bookId);
    }

    // 장바구니 비우기
    @PostMapping("/flush")
    @ResponseBody
    public void flushCart() {
        this.cartService.flushCart();
    }
}
