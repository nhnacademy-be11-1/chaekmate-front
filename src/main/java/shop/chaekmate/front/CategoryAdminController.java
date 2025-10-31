package shop.chaekmate.front;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.dto.request.CategoryCreateRequest;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CoreClient coreClient;

    @GetMapping
    public String categoryManagementForm(Model model) {
        model.addAttribute("allCategories", coreClient.getAllCategories());
        model.addAttribute("categoryCreateRequest", new CategoryCreateRequest());
        return "admin/manage-categories";
    }

    @PostMapping
    public String addCategory(@Valid CategoryCreateRequest categoryCreateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력값이 유효하지 않습니다. 이름은 255자를 초과할 수 없습니다.");
            return "redirect:/admin/categories";
        }
        coreClient.createCategory(categoryCreateRequest);
        redirectAttributes.addFlashAttribute("successMessage", "새 카테고리가 추가되었습니다.");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{categoryId}/update")
    public String updateCategory(@PathVariable Long categoryId, @Valid CategoryCreateRequest categoryCreateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력값이 유효하지 않습니다. 이름은 255자를 초과할 수 없습니다.");
            return "redirect:/admin/categories";
        }
        coreClient.updateCategory(categoryId, categoryCreateRequest);
        redirectAttributes.addFlashAttribute("successMessage", "카테고리가 수정되었습니다.");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{categoryId}/delete")
    public String deleteCategory(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            coreClient.deleteCategory(categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "카테고리 삭제 중 오류가 발생했습니다. 하위 카테고리가 없는지 확인해주세요.");
        }
        return "redirect:/admin/categories";
    }
}
