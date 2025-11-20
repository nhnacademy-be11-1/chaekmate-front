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
import org.springframework.web.bind.annotation.ResponseBody;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.point.dto.response.MemberPointHistoryResponse;
import shop.chaekmate.front.point.dto.response.PointResponse;
import shop.chaekmate.front.point.service.PointHistoryService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final PointHistoryService pointHistoryService;

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
}

