package shop.chaekmate.front.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.member.adaptor.MemberAdaptor;
import shop.chaekmate.front.member.dto.request.AddressCreateRequest;

import java.util.Map;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberProxyController {

    private final MemberAdaptor memberAdaptor;

    @ResponseBody
    @GetMapping("/check-login-id")
    public Map<String, Object> checkLoginId(@RequestParam String loginId) {
        return memberAdaptor.checkLoginId(loginId).data();
    }

    @ResponseBody
    @GetMapping("/check-email")
    public Map<String, Object> checkEmail(@RequestParam String email) {
        return memberAdaptor.checkEmail(email).data();
    }

    @PostMapping("/{memberId}/addresses")
    public String create(@PathVariable String memberId,
                         @Valid @ModelAttribute("addressCreateRequest") AddressCreateRequest request,
                         RedirectAttributes redirectAttributes) {
        memberAdaptor.createAddress(Long.valueOf(memberId), request); // core 호출
        redirectAttributes.addFlashAttribute("msg", "배송지가 등록되었습니다.");
        return "redirect:/" + memberId + "/mypage";
    }

    @DeleteMapping("/{memberId}/addresses/{addressId}")
    public String deleteAddress(@PathVariable String memberId,
                         @PathVariable String addressId,
                         RedirectAttributes redirectAttributes) {
        memberAdaptor.deleteAddress(Long.valueOf(memberId), Long.valueOf(addressId));
        redirectAttributes.addFlashAttribute("msg", "배송지가 삭제되었습니다.");
        return "redirect:/" + memberId + "/mypage";
    }

    @PostMapping("/{memberId}/withdraw")
    public String deleteMember(@PathVariable String memberId, RedirectAttributes redirectAttributes) {
        memberAdaptor.deleteMember(Long.valueOf(memberId));
        redirectAttributes.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다.");
        return "redirect:/login";
    }
}
