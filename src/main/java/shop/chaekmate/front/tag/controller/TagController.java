package shop.chaekmate.front.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.tag.dto.response.TagPageResponse;
import shop.chaekmate.front.tag.dto.response.TagResponse;
import shop.chaekmate.front.tag.service.TagService;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping(value = "/admin/tags")
    public String tagManagementView(@PageableDefault Pageable pageable, Model model){

        TagPageResponse<TagResponse> response = tagService.getPagedTags(pageable.getPageNumber(), pageable.getPageSize());

        model.addAttribute("pagedTags", response.content());
        model.addAttribute("totalPages", response.totalPages());
        model.addAttribute("pageNumber",response.pageNumber());
        model.addAttribute("pageSize",response.pageSize());
        model.addAttribute("hasPrevious", response.hasPrevious());
        model.addAttribute("hasNext", response.hasNext());
        return "admin/tag/tag-management";
    }

    @GetMapping("/admin/tags/new")
    public String tagManagementAddView(){
        return "admin/tag/tag-management-add";
    }

    @DeleteMapping("/admin/tags/{id}")
    public String deleteTag(@PathVariable("id") Long id) {

        tagService.deleteTagById(id);

        return "redirect:/admin/tags";
    }

    @PostMapping("/admin/tags")
    public String createTag(@RequestParam String name){

        tagService.createTag(name);

        return "redirect:/admin/tags";
    }
    
    
}
