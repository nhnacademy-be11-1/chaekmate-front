package shop.chaekmate.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.category.cache.CategoryCache;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final CategoryCache categoryCache;

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("categories", categoryCache.getCachedCategories());

        return "index";
    }
}
