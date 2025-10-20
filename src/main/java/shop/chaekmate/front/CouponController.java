package shop.chaekmate.front;

import com.nhnacademy.templateexample.dto.Category;
import com.nhnacademy.templateexample.dto.Coupon;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CouponController {

    private List<Category> mockCategories() {
        // Mock hierarchical categories
        Category fiction = new Category(1L, "Fiction");
        Category nonFiction = new Category(2L, "Non-Fiction");

        Category fantasy = new Category(3L, "Fantasy");
        Category scienceFiction = new Category(4L, "Science Fiction");
        fiction.addChild(fantasy);
        fiction.addChild(scienceFiction);

        Category biography = new Category(5L, "Biography");
        Category history = new Category(6L, "History");
        nonFiction.addChild(biography);
        nonFiction.addChild(history);

        return Arrays.asList(fiction, nonFiction, fantasy, scienceFiction, biography, history);
    }

    @ModelAttribute("topLevelCategories")
    public List<Category> populateCategories() {
        return mockCategories().stream().filter(c -> c.getChildren().isEmpty() && c.getId() < 3L).collect(Collectors.toList()); // Only top-level for display
    }

    @ModelAttribute("cartItemCount")
    public int populateCartItemCount() {
        return 0; // Mock count
    }

    @ModelAttribute("wishlistItemCount")
    public int populateWishlistItemCount() {
        return 0; // Mock count
    }

    @GetMapping("/coupons")
    public String viewCoupons(Model model) {
        // Mock coupons
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon("WELCOME10", "10% off your first order", 0.10, LocalDate.now().plusMonths(1)));
        coupons.add(new Coupon("BOOKLOVER5", "$5 off any book over $50", 5.00, LocalDate.now().plusMonths(2)));

        model.addAttribute("coupons", coupons);
        return "coupons";
    }
}
