package shop.chaekmate.front;

import shop.chaekmate.front.dto.Book;
import shop.chaekmate.front.dto.Category;
import shop.chaekmate.front.dto.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {

    private List<Book> mockBooks() {
        // Mock data for books
        return Arrays.asList(
                new Book(1L, "The Lord of the Rings", "The Fellowship of the Ring", "The first part of Tolkien's epic masterpiece.", "J.R.R. Tolkien", "Allen & Unwin", LocalDateTime.of(1954, 7, 29, 0, 0), "978-0618053267", 25000, 22990, "/images/book1.jpg", true, false, 100, 1200L, Arrays.asList(3L), Arrays.asList(1L, 4L)),
                new Book(2L, "Pride and Prejudice", "A classic of English literature", "A classic novel by Jane Austen.", "Jane Austen", "T. Egerton, Whitehall", LocalDateTime.of(1813, 1, 28, 0, 0), "978-0141439518", 18000, 15990, "/images/book2.jpg", false, false, 150, 800L, Arrays.asList(1L), Arrays.asList(1L, 2L)),
                new Book(3L, "To Kill a Mockingbird", "A novel about the serious issues of rape and racial inequality", "A novel by Harper Lee published in 1960.", "Harper Lee", "J. B. Lippincott & Co.", LocalDateTime.of(1960, 7, 11, 0, 0), "978-0446310789", 14000, 12500, "/images/book3.jpg", true, false, 80, 2000L, Arrays.asList(1L), List.of(1L)),
                new Book(4L, "1984", "A dystopian social science fiction novel", "A dystopian social science fiction novel by George Orwell.", "George Orwell", "Secker & Warburg", LocalDateTime.of(1949, 6, 8, 0, 0), "978-0451524935", 12000, 10990, "/images/book4.jpg", false, false, 200, 1500L, Arrays.asList(4L), List.of(3L)),
                new Book(5L, "A Brief History of Time", "From the Big Bang to Black Holes", "A landmark volume in science writing by one of the great minds of our time.", "Stephen Hawking", "Bantam Books", LocalDateTime.of(1988, 4, 1, 0, 0), "978-0553380163", 20000, 18990, "/images/book5.jpg", true, false, 120, 900L, Arrays.asList(6L), new ArrayList<>()),
                new Book(6L, "The Hobbit", "There and Back Again", "A fantasy novel and children's book by J. R. R. Tolkien.", "J.R.R. Tolkien", "George Allen & Unwin", LocalDateTime.of(1937, 9, 21, 0, 0), "978-0345339683", 16000, 14990, "/images/book6.jpg", true, false, 90, 1800L, Arrays.asList(3L), List.of(4L))
        );
    }

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

    private List<Tag> mockTags() {
        // Mock tags
        return Arrays.asList(
                new Tag(1L, "Classic"),
                new Tag(2L, "Romance"),
                new Tag(3L, "Dystopian"),
                new Tag(4L, "Adventure")
        );
    }

    @ModelAttribute("topLevelCategories")
    public List<Category> populateCategories() {
        return mockCategories().stream().filter(c -> c.getChildren().isEmpty() && c.getId() < 3L).collect(Collectors.toList()); // Only top-level for display
    }

    @ModelAttribute("cartItemCount")
    public int populateCartItemCount() {
        return 3; // Mock count
    }

    @ModelAttribute("wishlistItemCount")
    public int populateWishlistItemCount() {
        return 5; // Mock count
    }

    @GetMapping("/books")
    public String allBooks(Model model) {
        model.addAttribute("books", mockBooks());
        model.addAttribute("pageTitle", "All Books");
        return "books";
    }

    @GetMapping("/books/category/{categoryId}")
    public String booksByCategory(@PathVariable long categoryId, Model model) {
        Category category = mockCategories().stream().filter(c -> c.getId() == categoryId).findFirst().orElse(null);
        if (category != null) {
            List<Book> filteredBooks = mockBooks().stream()
                    .filter(book -> book.categoryIds().contains(categoryId) || (category.getChildren().stream().anyMatch(child -> child.getId() == book.categoryIds().get(0)))) // Assuming single category for simplicity in mock
                    .collect(Collectors.toList());
            model.addAttribute("books", filteredBooks);
            model.addAttribute("pageTitle", "Books in Category: " + category.getName());
        } else {
            model.addAttribute("pageTitle", "Category not found");
        }
        return "books";
    }

    @GetMapping("/books/tag/{tagId}")
    public String booksByTag(@PathVariable long tagId, Model model) {
        Tag tag = mockTags().stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
        if (tag != null) {
            List<Book> filteredBooks = mockBooks().stream()
                    .filter(book -> book.tagIds().contains(tagId))
                    .collect(Collectors.toList());
            model.addAttribute("books", filteredBooks);
            model.addAttribute("pageTitle", "Books with Tag: " + tag.getName());
        } else {
            model.addAttribute("pageTitle", "Tag not found");
        }
        return "books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam String query, Model model) {
        List<Book> results = mockBooks().stream()
                .filter(book -> book.title().toLowerCase().contains(query.toLowerCase()) ||
                                 book.author().toLowerCase().contains(query.toLowerCase()))
                .toList();
        model.addAttribute("books", results);
        model.addAttribute("pageTitle", "Search Results for: " + query);
        return "books";
    }

    @GetMapping("/book/{id}")
    public String bookDetail(@PathVariable long id, Model model) {
        Book book = mockBooks().stream().filter(b -> b.id() == id).findFirst().orElse(null);
        if (book != null) {
            model.addAttribute("book", book);
            return "book-detail";
        } else {
            return "redirect:/books";
        }
    }
}
