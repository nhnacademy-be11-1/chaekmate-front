package shop.chaekmate.front.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.auth.dto.response.PaycoTempInfoResponse;
import shop.chaekmate.front.auth.service.AuthService;
import shop.chaekmate.front.member.dto.request.AddressCreateRequest;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;
import shop.chaekmate.front.member.dto.response.GradeResponse;
import shop.chaekmate.front.member.dto.response.MemberAddressResponse;
import shop.chaekmate.front.member.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/signup")
    public String signupView() {
        return "member/signup";
    }

    @GetMapping("/signup/payco")
    public String paycoSignupView(@RequestParam String tempKey, Model model) {
        try {
            ResponseEntity<PaycoTempInfoResponse> response = authService.getPaycoTempInfo(tempKey);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PaycoTempInfoResponse paycoInfo = response.getBody();
                model.addAttribute("paycoInfo", paycoInfo);
            }
        } catch (Exception e) {
            log.error("PAYCO 정보 조회 실패", e);
        }
        return "member/payco-signup";
    }

    @PostMapping("/members")
    public String signupPost(@RequestBody MemberCreateRequest request,
                             @RequestParam(value = "paycoTempKey", required = false) String paycoTempKey,
                             HttpServletResponse response){
        log.info("request : {}", request);
        memberService.createMember(request);


        // PAYCO 회원가입인 경우 (loginId가 UUID 형식)
        boolean isPaycoSignup = request.loginId().matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

        if (isPaycoSignup) {
            try {
                // PAYCO 임시 정보 삭제
                if (paycoTempKey != null && !paycoTempKey.isEmpty()) {
                    authService.deletePaycoTempInfo(paycoTempKey);
                }

                // PAYCO 자동 로그인 처리
                ResponseEntity<shop.chaekmate.front.auth.dto.response.LoginResponse> loginResponse =
                        authService.paycoAutoLogin(request.loginId());

                // Set-Cookie 헤더 추출하여 브라우저에 전달
                if (loginResponse.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
                    List<String> cookieHeaders = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
                    if (cookieHeaders != null) {
                        cookieHeaders.forEach(cookieHeader ->
                                response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader));
                    }
                }

                // 로그인 성공: 홈으로 리다이렉트
                return "redirect:/";
            } catch (Exception e) {
                log.error("PAYCO 자동 로그인 실패", e);
                return "redirect:/login";
            }
        }

        // 일반 회원가입: 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }

    @GetMapping("/members/{memberId}/mypage")
    public String mypageView(@PathVariable String memberId, Model model){
        List<MemberAddressResponse> addresses = memberService.getAddressesByMemberId(memberId);
        GradeResponse memberGrade = memberService.getGradeByMemberId(memberId);
        List<GradeResponse> grades = memberService.getAllGrades();
        model.addAttribute("memberId", memberId);
        model.addAttribute("addresses", addresses == null ? java.util.List.of() : addresses);
        model.addAttribute("addressCreateRequest", new AddressCreateRequest("", "", "", 0));
        model.addAttribute("memberGrade", memberGrade);
        model.addAttribute("grades", grades);
        return "member/mypage";
    }
}
