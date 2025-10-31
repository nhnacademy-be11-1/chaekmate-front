package shop.chaekmate.front.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.book.dto.request.TagCreateRequest;
import shop.chaekmate.front.common.CoreClient;

@Controller
@RequiredArgsConstructor
public class AdminTagController {

    private final CoreClient coreClient;

    // Tag Admin Methods
    @GetMapping("/admin/tags")
    public String manageTags(Model model) {
        model.addAttribute("tags", coreClient.getAllTags());
        return "admin/manage-tags";
    }

    @PostMapping("/admin/tags")
    public String addTag(TagCreateRequest tagCreateRequest, RedirectAttributes redirectAttributes) {
        coreClient.createTag(tagCreateRequest);
        redirectAttributes.addFlashAttribute("successMessage", "새 태그가 추가되었습니다.");
        return "redirect:/admin/tags";
    }

    @PostMapping("/admin/tags/{tagId}/delete")
    public String deleteTag(@PathVariable Long tagId, RedirectAttributes redirectAttributes) {
        try {
            coreClient.deleteTag(tagId);
            redirectAttributes.addFlashAttribute("successMessage", "태그가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "태그 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/admin/tags/{tagId}/update")
    public String updateTag(@PathVariable Long tagId, TagCreateRequest tagCreateRequest,
                            RedirectAttributes redirectAttributes) {
        try {
            coreClient.updateTag(tagId, tagCreateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "태그가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "태그 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/tags";
    }
}
