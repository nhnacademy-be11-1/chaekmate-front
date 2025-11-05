package shop.chaekmate.front.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.chaekmate.front.category.dto.response.CategoryHierarchyResponse;
import shop.chaekmate.front.category.service.CategoryService;
import shop.chaekmate.front.category.dto.response.CategoryPageResponse;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String categoryManagementView(@PageableDefault Pageable pageable, Model model) {
        CategoryPageResponse<CategoryHierarchyResponse> response = categoryService.getPagedCategories(pageable.getPageNumber(), pageable.getPageSize());

        model.addAttribute("pagedCategories", response.content());
        model.addAttribute("totalPages", response.totalPages());
        model.addAttribute("pageNumber",response.pageNumber());
        model.addAttribute("pageSize",response.pageSize());
        model.addAttribute("hasPrevious", response.hasPrevious());
        model.addAttribute("hasNext", response.hasNext());
        return "admin/category/category-management";
    }

    @GetMapping("/admin/categories/new")
    public String categoryManagementAddView(){
        return "admin/category/category-management-add";
    }

    @DeleteMapping("/admin/categories/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {

        categoryService.deleteCategoryById(id);

        return "redirect:/admin/categories/";
    }

}
