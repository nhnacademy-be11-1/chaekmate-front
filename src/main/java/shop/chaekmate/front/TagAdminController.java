package shop.chaekmate.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.dto.request.TagCreateRequest;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class TagAdminController {

    private final CoreClient coreClient;

    @GetMapping
    public String manageTags(Model model) {
        model.addAttribute("tags", coreClient.getAllTags());
        return "admin/manage-tags";
    }

    @PostMapping
    public String addTag(TagCreateRequest tagCreateRequest, RedirectAttributes redirectAttributes) {
        coreClient.createTag(tagCreateRequest);
        redirectAttributes.addFlashAttribute("successMessage", "새 태그가 추가되었습니다.");
        return "redirect:/admin/tags";
    }

    @PostMapping("/{tagId}/delete")
    public String deleteTag(@PathVariable Long tagId, RedirectAttributes redirectAttributes) {
        try {
            coreClient.deleteTag(tagId);
            redirectAttributes.addFlashAttribute("successMessage", "태그가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "태그 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/{tagId}/update")
    public String updateTag(@PathVariable Long tagId, TagCreateRequest tagCreateRequest, RedirectAttributes redirectAttributes) {
        try {
            coreClient.updateTag(tagId, tagCreateRequest);
            redirectAttributes.addFlashAttribute("successMessage", "태그가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "태그 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/tags";
    }
}
