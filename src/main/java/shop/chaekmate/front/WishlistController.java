package shop.chaekmate.front;

import shop.chaekmate.front.dto.Book;
import shop.chaekmate.front.dto.Category;
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
public class WishlistController {

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
        return 2; // Mock count
    }

    @GetMapping("/wishlist")
    public String viewWishlist(Model model) {
        // Mock wishlist items
        List<Book> wishlistItems = new ArrayList<>();
        wishlistItems.add(new Book(1L, "The Lord of the Rings", "J.R.R. Tolkien", 22.99, "/images/book1.jpg", "Description", 3L, new ArrayList<>()));
        wishlistItems.add(new Book(5L, "A Brief History of Time", "Stephen Hawking", 18.99, "/images/book5.jpg", "Description", 6L, new ArrayList<>()));

        model.addAttribute("wishlistItems", wishlistItems);
        return "wishlist";
    }

    @PostMapping("/wishlist/add")
    public String addToWishlist(@RequestParam long bookId) {
        System.out.println("Adding book " + bookId + " to wishlist (mock)");
        return "redirect:/wishlist";
    }
}