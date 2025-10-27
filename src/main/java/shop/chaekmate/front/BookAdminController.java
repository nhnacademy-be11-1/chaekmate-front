package shop.chaekmate.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.dto.request.BookCreateRequest;

@Controller
@RequiredArgsConstructor
public class BookAdminController {

    private final CoreClient coreClient;

    @GetMapping("/admin/books/add")
    public String addBookForm() {
        return "admin/add-book";
    }

    @PostMapping("/add")
    public String addBook(BookCreateRequest bookCreateRequest, @RequestPart("image") MultipartFile image) {
        coreClient.createBook(bookCreateRequest, image);
        return "redirect:/";
    }
}
