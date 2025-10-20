package shop.chaekmate.front;

import com.nhnacademy.templateexample.dto.Book;
import com.nhnacademy.templateexample.dto.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @ModelAttribute("topLevelCategories")
    public List<Category> populateCategories() {
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

        return Arrays.asList(fiction, nonFiction);
    }

    @ModelAttribute("cartItemCount")
    public int populateCartItemCount() {
        // Mock cart item count
        return 3;
    }

    @ModelAttribute("wishlistItemCount")
    public int populateWishlistItemCount() {
        // Mock wishlist item count
        return 5;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Mock bestsellers data
        List<Book> bestSellers = new ArrayList<>();
        bestSellers.add(new Book(1L, "The Lord of the Rings", "J.R.R. Tolkien", 22.99, "/images/book1.jpg", "The first part of Tolkien's epic masterpiece.", 3L, Arrays.asList(1L, 4L)));
        bestSellers.add(new Book(2L, "Pride and Prejudice", "Jane Austen", 15.99, "/images/book2.jpg", "A classic novel by Jane Austen.", 1L, Arrays.asList(1L, 2L)));
        bestSellers.add(new Book(3L, "To Kill a Mockingbird", "Harper Lee", 12.50, "/images/book3.jpg", "A novel by Harper Lee published in 1960.", 1L, List.of(1L)));
        bestSellers.add(new Book(4L, "1984", "George Orwell", 10.99, "/images/book4.jpg", "A dystopian social science fiction novel by George Orwell.", 4L, List.of(3L)));

        model.addAttribute("bestSellers", bestSellers);
        return "index";
    }
}
