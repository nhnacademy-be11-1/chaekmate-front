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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {

    private List<Book> mockBooks() {
        // Mock data for books
        return Arrays.asList(
                new Book(1L, "The Lord of the Rings", "J.R.R. Tolkien", 22.99, "/images/book1.jpg", "The first part of Tolkien's epic masterpiece.", 3L, Arrays.asList(1L, 4L)),
                new Book(2L, "Pride and Prejudice", "Jane Austen", 15.99, "/images/book2.jpg", "A classic novel by Jane Austen.", 1L, Arrays.asList(1L, 2L)),
                new Book(3L, "To Kill a Mockingbird", "Harper Lee", 12.50, "/images/book3.jpg", "A novel by Harper Lee published in 1960.", 1L, List.of(1L)),
                new Book(4L, "1984", "George Orwell", 10.99, "/images/book4.jpg", "A dystopian social science fiction novel by George Orwell.", 4L, List.of(3L)),
                new Book(5L, "A Brief History of Time", "Stephen Hawking", 18.99, "/images/book5.jpg", "A landmark volume in science writing by one of the great minds of our time.", 6L, new ArrayList<>()),
                new Book(6L, "The Hobbit", "J.R.R. Tolkien", 14.99, "/images/book6.jpg", "A fantasy novel and children's book by J. R. R. Tolkien.", 3L, List.of(4L))
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
                    .filter(book -> book.categoryId() == categoryId || (category.getChildren().stream().anyMatch(child -> child.getId() == book.categoryId())))
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
