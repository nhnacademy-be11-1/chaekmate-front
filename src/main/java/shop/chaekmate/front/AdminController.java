package shop.chaekmate.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AdminController {

    @ModelAttribute("cartItemCount")
    public int populateCartItemCount() {
        return 0; // Mock count
    }

    @ModelAttribute("wishlistItemCount")
    public int populateWishlistItemCount() {
        return 0; // Mock count
    }

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }
}
