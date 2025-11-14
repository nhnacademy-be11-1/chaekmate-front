package shop.chaekmate.front.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.member.dto.request.AddressCreateRequest;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;
import shop.chaekmate.front.member.dto.response.MemberAddressResponse;
import shop.chaekmate.front.member.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupView(){
        return "/member/signup";
    }

    @PostMapping("/members")
    public String signupPost(@RequestBody MemberCreateRequest request){
        log.info("request : {}", request);
        memberService.createMember(request);
        return "redirect:/login";
    }

    @GetMapping("/{memberId}/mypage")
    public String mypageView(@PathVariable String memberId, Model model){
        List<MemberAddressResponse> addresses = memberService.getAddressesByMemberId(memberId);
        model.addAttribute("memberId", memberId);
        model.addAttribute("addresses", addresses == null ? java.util.List.of() : addresses);
        model.addAttribute("addressCreateRequest", new AddressCreateRequest("", "", "", 0));
        return "/member/mypage";
    }
}