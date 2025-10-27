package shop.chaekmate.front;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.dto.request.CategoryCreateRequest;
import shop.chaekmate.front.dto.response.CategoryCreateResponse;
import shop.chaekmate.front.dto.response.CategoryResponse;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CoreClient coreClient;

    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        List<CategoryResponse> categories = coreClient.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/add-category";
    }

    @PostMapping("/add")
    public String addCategory(@Valid CategoryCreateRequest categoryCreateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // For simplicity, just redirecting with a generic error message.
            // A real implementation would handle errors more gracefully.
            redirectAttributes.addFlashAttribute("errorMessage", "입력값이 유효하지 않습니다. 이름은 255자를 초과할 수 없습니다.");
            return "redirect:/admin/categories/add";
        }
        CategoryCreateResponse newCategory = coreClient.createCategory(categoryCreateRequest);
        redirectAttributes.addFlashAttribute("newCategoryMessage",
                "새 카테고리가 추가되었습니다: [ID: " + newCategory.getId() + ", 이름: " + newCategory.getName() + "]");
        return "redirect:/admin/categories/add";
    }
}
