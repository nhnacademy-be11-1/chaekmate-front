package shop.chaekmate.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import shop.chaekmate.front.book.dto.response.CategoryResponse;
import shop.chaekmate.front.common.CoreClient;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final CoreClient coreClient;

    @ModelAttribute("allCategories")
    public List<CategoryResponse> populateCategories() {
        return coreClient.getAllCategories();
    }

    @GetMapping("/")
    public String index(Model model) {

        return "index";
    }
}
