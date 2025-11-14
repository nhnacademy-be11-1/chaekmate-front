package shop.chaekmate.front.member.controller;

import feign.FeignException;
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
    private static final int MAX_ADDRESS_COUNT = 10;

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
    public String createAddress(@PathVariable String memberId,
                         @Valid @ModelAttribute("addressCreateRequest") AddressCreateRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            memberAdaptor.createAddress(Long.valueOf(memberId), request); // core 호출

            redirectAttributes.addFlashAttribute("msg", "배송지가 등록되었습니다.");
        } catch (FeignException e) {
            // 상태코드로 주소 개수 초과 구분 (400/409 등 프로젝트에 맞게)
            int status = e.status();

            if (status == 400 || status == 409) {
                redirectAttributes.addFlashAttribute("msg", "배송지는 최대 " +  MAX_ADDRESS_COUNT + "개까지 등록할 수 있습니다.");
            } else {
                redirectAttributes.addFlashAttribute("msg", "주소 등록 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            }
        }
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
