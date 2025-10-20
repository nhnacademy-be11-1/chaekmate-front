package shop.chaekmate.front;

import com.nhnacademy.templateexample.dto.Book;
import com.nhnacademy.templateexample.dto.CartItem;
import com.nhnacademy.templateexample.dto.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartController {

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
        return 2; // Mock count
    }

    @ModelAttribute("wishlistItemCount")
    public int populateWishlistItemCount() {
        return 1; // Mock count
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        // Mock cart items
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Book(1L, "The Lord of the Rings", "J.R.R. Tolkien", 22.99, "/images/book1.jpg", "Description", 3L, new ArrayList<>()), 1));
        cartItems.add(new CartItem(new Book(2L, "Pride and Prejudice", "Jane Austen", 15.99, "/images/book2.jpg", "Description", 1L, new ArrayList<>()), 2));

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartItems.stream().mapToDouble(CartItem::getSubtotal).sum());
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam long bookId) {
        // In a real app, this would add to a session-based cart or call a backend service
        System.out.println("Adding book " + bookId + " to cart (mock)");
        return "redirect:/cart";
    }
}
