package shop.chaekmate.front.common;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.chaekmate.front.book.service.LikeService;
import shop.chaekmate.front.cart.dto.response.CartItemCountResponse;
import shop.chaekmate.front.cart.service.CartService;
import shop.chaekmate.front.cart.util.GuestIdResolver;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.category.service.CategoryService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeHandler {

    private final CategoryService categoryService; // Service에서 캐시 관리
    private final LikeService likeService;
    private final CartService cartService;
    private final GuestIdResolver guestIdResolver;

    // 모든 페이지에 카테고리들 주입
    @ModelAttribute("categories")
    public List<CategoryResponse> addCategoriesToModel() {
        // Service가 캐시 확인 후 API 호출까지 알아서 처리
        return categoryService.getCategories();
    }

    // 좋아요 목록 주입
    @ModelAttribute("likedBookIds")
    public List<Long> getMemberLikedBookIds(){
        return likeService.getMemberLikedBook();
    }

    // 모든 페이지 헤더에 장바구니 아이템 개수 주입
    @ModelAttribute("cartItemCount")
    public CartItemCountResponse addCartItemCountToModel(HttpServletRequest request, HttpServletResponse response) {
        String guestId = this.guestIdResolver.getOrCreateUuid(request, response);
        return this.cartService.getCartItemCount(guestId);
    }

}
