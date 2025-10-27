package shop.chaekmate.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.dto.request.TagCreateRequest;
import shop.chaekmate.front.dto.response.TagCreateResponse;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class TagAdminController {

    private final CoreClient coreClient;

    @GetMapping("/add")
    public String addTagForm() {
        return "admin/add-tag";
    }

    @PostMapping("/add")
    public String addTag(TagCreateRequest tagCreateRequest, RedirectAttributes redirectAttributes) {
        TagCreateResponse newTag = coreClient.createTag(tagCreateRequest);
        redirectAttributes.addFlashAttribute("newTagMessage",
                "새 태그가 추가되었습니다: [ID: " + newTag.getId() + ", 이름: " + newTag.getName() + "]");
        return "redirect:/admin/tags/add";
    }
}
