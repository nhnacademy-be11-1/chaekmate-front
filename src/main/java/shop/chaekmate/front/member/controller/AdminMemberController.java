package shop.chaekmate.front.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.member.dto.response.MemberResponse;
import shop.chaekmate.front.member.service.AdminMemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    private static final String REDIRECT_MEMBERS = "redirect:/admin/members";

    private final AdminMemberService adminMemberService;

    @GetMapping("/admin/members")
    public String memberManagementView(@RequestParam(defaultValue = "ACTIVE") String status,
                                       Model model) {
        List<MemberResponse> members = "DELETED".equalsIgnoreCase(status)
                ? adminMemberService.getDeletedMembers()
                : adminMemberService.getActiveMembers();

        model.addAttribute("members", members);
        model.addAttribute("status", status.toUpperCase());
        return "admin/member/member-management";
    }

    @PostMapping("/admin/members/{memberId}")
    public String deleteMember(@PathVariable Long memberId,
                               @RequestParam(defaultValue = "ACTIVE") String status,
                               RedirectAttributes redirectAttributes) {
        try {
            adminMemberService.deleteMember(memberId);
            redirectAttributes.addFlashAttribute("msg", "회원이 탈퇴 처리되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "회원 탈퇴 처리에 실패했습니다.");
        }
        redirectAttributes.addAttribute("status", status.toUpperCase());
        return REDIRECT_MEMBERS;
    }

    @PostMapping("/admin/members/{memberId}/restore")
    public String restoreMember(@PathVariable Long memberId,
                                @RequestParam(defaultValue = "DELETED") String status,
                                RedirectAttributes redirectAttributes) {
        try {
            adminMemberService.restoreMember(memberId);
            redirectAttributes.addFlashAttribute("msg", "회원이 복구되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "회원 복구에 실패했습니다.");
        }
        redirectAttributes.addAttribute("status", status.toUpperCase());
        return REDIRECT_MEMBERS;
    }
}