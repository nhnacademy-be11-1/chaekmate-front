package shop.chaekmate.front.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.member.dto.request.AddressCreateRequest;
import shop.chaekmate.front.member.dto.request.UpdateMemberRequest;
import shop.chaekmate.front.member.dto.request.VerifyPasswordRequest;
import shop.chaekmate.front.member.dto.response.GradeResponse;
import shop.chaekmate.front.member.dto.response.MemberAddressResponse;
import shop.chaekmate.front.member.dto.response.MemberResponse;
import shop.chaekmate.front.member.service.MemberService;
import shop.chaekmate.front.point.dto.response.MemberPointHistoryResponse;
import shop.chaekmate.front.point.dto.response.PointResponse;
import shop.chaekmate.front.point.service.PointHistoryService;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final PointHistoryService pointHistoryService;
    private final MemberService memberService;

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomPrincipal principal, Model model) {
        log.info("===== 마이페이지 접근 시도 =====");
        log.info("Principal: {}", principal);

        if (principal == null) {
            log.error("Principal이 null입니다. 로그인 페이지로 리다이렉트합니다.");
            return "redirect:/login";
        }

        log.info("마이페이지 접근 성공: 회원ID={}, 이름={}", principal.getMemberId(), principal.getName());

        // 회원 정보
        model.addAttribute("memberId", principal.getMemberId());
        model.addAttribute("memberName", principal.getName());

        // 주소 정보
        List<MemberAddressResponse> addresses = memberService.getAddressesByMemberId(principal.getMemberId());
        model.addAttribute("addresses", addresses == null ? List.of() : addresses);
        model.addAttribute("addressCreateRequest", new AddressCreateRequest("", "", "", 0));

        // 등급 정보
        GradeResponse memberGrade = memberService.getGradeByMemberId(principal.getMemberId());
        List<GradeResponse> grades = memberService.getAllGrades();
        model.addAttribute("memberGrade", memberGrade);
        model.addAttribute("grades", grades);

        return "member/mypage";
    }

    // AJAX로 포인트 히스토리 조회
    @GetMapping("/api/mypage/point-histories")
    @ResponseBody
    public Page<MemberPointHistoryResponse> getMyPointHistories(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PageableDefault(size = 10) Pageable pageable) {

        if (principal == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        log.info("회원 {} 포인트 히스토리 조회 (AJAX)", principal.getMemberId());
        return pointHistoryService.getMemberPointHistory(principal.getMemberId(), pageable);
    }

    // AJAX로 포인트 잔액 조회
    @GetMapping("/api/mypage/points")
    @ResponseBody
    public PointResponse getMyPoint(
            @AuthenticationPrincipal CustomPrincipal principal) {

        if (principal == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        log.info("회원 {} 포인트 잔액 조회 (AJAX)", principal.getMemberId());
        return pointHistoryService.getMemberPoint(principal.getMemberId());
    }


    @GetMapping("/profile")
    public String profileView(@AuthenticationPrincipal CustomPrincipal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Long memberId = principal.getMemberId();
        MemberResponse member = memberService.getMemberById(memberId);

        model.addAttribute("memberId", memberId);
        model.addAttribute("member", member);

        model.addAttribute("currentPage", "member-profile");

        return "member/member-profile";
    }

    @PostMapping("/profile")
    public String updateMember(@AuthenticationPrincipal CustomPrincipal principal,
                               UpdateMemberRequest request,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        Long memberId = principal.getMemberId();

        try {
            memberService.updateMember(memberId, request);
            redirectAttributes.addFlashAttribute("msg", "회원 정보가 수정되었습니다.");
        } catch (Exception e) {
            // 백엔드에서 비밀번호 불일치, 이메일 중복 등의 예외를 던졌다고 가정
            log.error("회원 정보 수정 실패", e);
            redirectAttributes.addFlashAttribute("msg", "회원 정보 수정에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/api/profile/verify-password")
    @ResponseBody
    public Map<String, Boolean> verifyPassword(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody VerifyPasswordRequest request
    ) {
        if (principal == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Long memberId = principal.getMemberId();
        boolean valid = memberService.verifyPassword(memberId, request);

        return Map.of("valid", valid);
    }
}

