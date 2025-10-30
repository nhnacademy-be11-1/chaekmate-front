package shop.chaekmate.front;

import shop.chaekmate.front.dto.Book;
import shop.chaekmate.front.dto.CartItem;
import shop.chaekmate.front.dto.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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
        cartItems.add(new CartItem(new Book(1L, "The Lord of the Rings", "The Fellowship of the Ring", "The first part of Tolkien's epic masterpiece.", "J.R.R. Tolkien", "Allen & Unwin", LocalDateTime.of(1954, 7, 29, 0, 0), "978-0618053267", 25000, 22990, "/images/book1.jpg", true, false, 100, 1200L, Arrays.asList(3L), Arrays.asList(1L, 4L)), 1));
        cartItems.add(new CartItem(new Book(2L, "Pride and Prejudice", "A classic of English literature", "A classic novel by Jane Austen.", "Jane Austen", "T. Egerton, Whitehall", LocalDateTime.of(1813, 1, 28, 0, 0), "978-0141439518", 18000, 15990, "/images/book2.jpg", false, false, 150, 800L, Arrays.asList(1L), Arrays.asList(1L, 2L)), 2));

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
