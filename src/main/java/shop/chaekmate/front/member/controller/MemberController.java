package shop.chaekmate.front.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;
import shop.chaekmate.front.member.dto.response.MemberGradeResponse;
import shop.chaekmate.front.member.service.MemberService;


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
        MemberGradeResponse response = memberService.getGradeByMemberId(memberId);
        model.addAttribute("grade",  response);
        return "/member/mypage";
    }
}