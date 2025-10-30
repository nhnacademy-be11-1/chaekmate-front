package shop.chaekmate.front;

import shop.chaekmate.front.dto.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

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
        bestSellers.add(new Book(1L, "The Lord of the Rings", "The Fellowship of the Ring", "The first part of Tolkien's epic masterpiece.", "J.R.R. Tolkien", "Allen & Unwin", LocalDateTime.of(1954, 7, 29, 0, 0), "978-0618053267", 25000, 22990, "/images/book1.jpg", true, false, 100, 1200L, Arrays.asList(3L), Arrays.asList(1L, 4L)));
        bestSellers.add(new Book(2L, "Pride and Prejudice", "A classic of English literature", "A classic novel by Jane Austen.", "Jane Austen", "T. Egerton, Whitehall", LocalDateTime.of(1813, 1, 28, 0, 0), "978-0141439518", 18000, 15990, "/images/book2.jpg", false, false, 150, 800L, Arrays.asList(1L), Arrays.asList(1L, 2L)));
        bestSellers.add(new Book(3L, "To Kill a Mockingbird", "A novel about the serious issues of rape and racial inequality", "A novel by Harper Lee published in 1960.", "Harper Lee", "J. B. Lippincott & Co.", LocalDateTime.of(1960, 7, 11, 0, 0), "978-0446310789", 14000, 12500, "/images/book3.jpg", true, false, 80, 2000L, Arrays.asList(1L), List.of(1L)));
        bestSellers.add(new Book(4L, "1984", "A dystopian social science fiction novel", "A dystopian social science fiction novel by George Orwell.", "George Orwell", "Secker & Warburg", LocalDateTime.of(1949, 6, 8, 0, 0), "978-0451524935", 12000, 10990, "/images/book4.jpg", false, false, 200, 1500L, Arrays.asList(4L), List.of(3L)));

        model.addAttribute("bestSellers", bestSellers);
        return "index";
    }
}
