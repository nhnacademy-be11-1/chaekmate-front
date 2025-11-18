package shop.chaekmate.front.cart.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.cart.dto.request.CartItemCreateRequest;
import shop.chaekmate.front.cart.dto.request.CartItemUpdateRequest;
import shop.chaekmate.front.cart.dto.response.CartItemListAdvancedResponse;
import shop.chaekmate.front.cart.dto.response.CartItemListResponse;
import shop.chaekmate.front.cart.dto.response.CartItemUpdateResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "cart-client", url = "${chaekmate.gateway.url}")
public interface CartAdaptor {

    // 장바구니 담기
    @PostMapping("/carts/items")
    CommonResponse<CartItemListResponse> addCartItem(@RequestBody CartItemCreateRequest request);

    // 장바구니 조회
    @GetMapping("/carts")
    CommonResponse<CartItemListAdvancedResponse> getCart();

    // 장바구니 아이템 수량 변경
    @PutMapping("/carts/items/{bookId}")
    CommonResponse<CartItemUpdateResponse> updateCartItem(@PathVariable(name = "bookId") Long bookId,
                                                          @RequestBody CartItemUpdateRequest request);

    // 장바구니 아이템 단일 삭제
    @DeleteMapping("/carts/items/{bookId}")
    CommonResponse<Void> deleteCartItem(@PathVariable(name = "bookId") Long bookId);

    // 장바구니 비우기
    @DeleteMapping("/carts/items")
    CommonResponse<Void> flushCart();
}
