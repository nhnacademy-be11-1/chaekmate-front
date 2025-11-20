package shop.chaekmate.front.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.member.dto.request.CreateGradeRequest;
import shop.chaekmate.front.member.dto.request.UpdateGradeRequest;
import shop.chaekmate.front.member.dto.response.GradeResponse;
import shop.chaekmate.front.member.service.AdminGradeService;
import shop.chaekmate.front.member.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminGradeController {

    private static final String REDIRECT_GRADES = "redirect:/admin/grades";

    private final AdminGradeService adminGradeService;
    private final MemberService memberService;

    @GetMapping("/admin/grades")
    public String gradeManagementView(Model model) {
        List<GradeResponse> grades = memberService.getAllGrades();
        model.addAttribute("grades", grades);
        return "admin/grade/grade-management";
    }

    @PostMapping("/admin/grades/{gradeId}")
    public String updateGrade(@PathVariable Long gradeId,
                              @RequestParam String name,
                              @RequestParam Byte pointRate,
                              @RequestParam int upgradeStandardAmount,
                              RedirectAttributes redirectAttributes) {
        try {
            UpdateGradeRequest request = new UpdateGradeRequest(name, pointRate, upgradeStandardAmount);
            adminGradeService.updateGrade(gradeId, request);
            redirectAttributes.addFlashAttribute("msg", "등급이 수정되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "등급 수정 실패");
        }
        return REDIRECT_GRADES;
    }

    @PostMapping("/admin/grades")
    public String createGrade(@RequestParam String name,
                              @RequestParam Byte pointRate,
                              @RequestParam int upgradeStandardAmount,
                              RedirectAttributes redirectAttributes) {
        try {
            CreateGradeRequest request = new CreateGradeRequest(name, pointRate, upgradeStandardAmount);
            adminGradeService.createGrade(request);
            redirectAttributes.addFlashAttribute("msg", "등급이 추가되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "등급 추가 실패");
        }
        return REDIRECT_GRADES;
    }

    @PostMapping("/admin/grades/{gradeId}/delete")
    public String deleteGrade(@PathVariable Long gradeId, RedirectAttributes redirectAttributes) {
        try {
            adminGradeService.deleteGrade(gradeId);
            redirectAttributes.addFlashAttribute("msg", "등급이 삭제되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "등급 삭제 실패");
        }
        return REDIRECT_GRADES;
    }

}
