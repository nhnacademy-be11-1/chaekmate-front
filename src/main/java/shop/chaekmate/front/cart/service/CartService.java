package shop.chaekmate.front.cart.service;

import feign.FeignException;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.cart.adaptor.CartAdaptor;
import shop.chaekmate.front.cart.dto.request.CartItemCreateRequest;
import shop.chaekmate.front.cart.dto.request.CartItemUpdateRequest;
import shop.chaekmate.front.cart.dto.response.CartItemListAdvancedResponse;
import shop.chaekmate.front.cart.dto.response.CartItemListResponse;
import shop.chaekmate.front.cart.dto.response.CartItemUpdateResponse;
import shop.chaekmate.front.cart.exception.CartException;
import shop.chaekmate.front.common.CommonResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartAdaptor cartAdaptor;

    // 장바구니 아이템 추가
    public CartItemListResponse addCartItem(CartItemCreateRequest request) {
        try {
            CommonResponse<CartItemListResponse> response = this.cartAdaptor.addCartItem(request);
            if (Objects.isNull(response) || Objects.isNull(response.data())) {
                throw new CartException("장바구니 담기 결과가 없습니다.");
            }
            return response.data();
        } catch (FeignException e) {
            log.error("장바구니 담기 실패: {}", e.getMessage(), e);
            throw new CartException("장바구니 담기 실패", e);
        }
    }

    // 장바구니 조회
    public CartItemListAdvancedResponse getCart() {
        try {
            CommonResponse<CartItemListAdvancedResponse> response = this.cartAdaptor.getCart();

            // API 응답이 없거나 data가 null인 경우
            if (Objects.isNull(response) || Objects.isNull(response.data())) {
                log.warn("장바구니 조회 결과가 없습니다. 빈 장바구니를 반환합니다.");
                return new CartItemListAdvancedResponse(null, Collections.emptyList());
            }

            // cartId가 null이면 장바구니가 아직 생성되지 않은 상태
            CartItemListAdvancedResponse cart = response.data();
            if (Objects.isNull(cart.cartId())) {
                log.info("장바구니가 아직 생성되지 않았습니다. 빈 장바구니를 반환합니다.");
            }

            return cart;

        } catch (FeignException e) {
            log.error("장바구니 조회 실패: {}", e.getMessage(), e);
            // 조회 실패 시에도 빈 장바구니 반환 (화면은 정상 표시)
            return new CartItemListAdvancedResponse(null, Collections.emptyList());
        }
    }

    // 장바구니 수량 변경
    public CartItemUpdateResponse updateCartItem(Long bookId, CartItemUpdateRequest request) {
        try {
            CommonResponse<CartItemUpdateResponse> response = this.cartAdaptor.updateCartItem(bookId, request);
            if (Objects.isNull(response) || Objects.isNull(response.data())) {
                throw new CartException("장바구니 상품 수량 수정 결과가 없습니다.");
            }
            return response.data();
        } catch (FeignException e) {
            log.error("장바구니 상품 수량 수정 실패: {}", e.getMessage(), e);
            throw new CartException("장바구니 상품 수량 수정 실패", e);
        }
    }

    // 장바구니 아이템 삭제
    public void deleteCartItem(Long bookId) {
        try {
            this.cartAdaptor.deleteCartItem(bookId);
        } catch (FeignException e) {
            log.error("장바구니 아이템 삭제 실패: {}", e.getMessage(), e);
            throw new CartException("장바구니 아이템 삭제 실패", e);
        }    }

    // 장바구니 비우기
    public void flushCart() {
        try {
            this.cartAdaptor.flushCart();
        } catch (FeignException e) {
            log.error("장바구니 비우기 실패: {}", e.getMessage(), e);
            throw new CartException("장바구니 비우기 실패", e);
        }    }

}
