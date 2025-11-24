package shop.chaekmate.front.point.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.point.dto.response.MemberPointHistoryResponse;
import shop.chaekmate.front.point.dto.response.PointHistoryResponse;
import shop.chaekmate.front.point.dto.response.PointResponse;
import shop.chaekmate.front.point.service.PointHistoryService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    @GetMapping("/admin/point-histories")
    public String getPointHistory(Model model, Pageable pageable) {
        log.info("===== GET /admin/point-histories 요청 받음 =====");
        log.info("Pageable: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<PointHistoryResponse> pointHistory =
                    pointHistoryService.getAllPointHistory(pageable);
            log.info("포인트 히스토리 데이터 개수: {}", pointHistory.getTotalElements());

            model.addAttribute("pointHistory", pointHistory);
            return "admin/point/point-histories-management";
        } catch (Exception e) {
            log.error("포인트 히스토리 페이지 로드 실패", e);
            // 에러 발생 시에도 빈 Page 객체를 추가하여 템플릿 에러 방지
            model.addAttribute("pointHistory", Page.empty());
            model.addAttribute("error", "페이지 로드 중 오류가 발생했습니다: " + e.getMessage());
            return "admin/point/point-histories-management";
        }
    }

    // 개인 포인트 조회 페이지
    @GetMapping("/members/point-histories")
    public String getMyPointHistory(
            @AuthenticationPrincipal CustomPrincipal principal,
            Model model,
            @PageableDefault(size = 10) Pageable pageable) {

        log.info("===== 개인 포인트 내역 조회 페이지 접근 =====");

        if (principal == null) {
            log.error("로그인이 필요합니다. 로그인 페이지로 리다이렉트합니다.");
            return "redirect:/login";
        }

        log.info("회원 {} 포인트 내역 페이지 접근", principal.getMemberId());

        try {
            // 포인트 잔액 조회
            PointResponse pointResponse = pointHistoryService.getMemberPoint(principal.getMemberId());
            model.addAttribute("point", pointResponse);

            // 포인트 내역 조회
            Page<MemberPointHistoryResponse> pointHistory =
                    pointHistoryService.getMemberPointHistory(principal.getMemberId(), pageable);
            model.addAttribute("pointHistory", pointHistory);
            model.addAttribute("memberId", principal.getMemberId());

            log.info("포인트 내역 데이터 개수: {}", pointHistory.getTotalElements());
            return "member/point-history";
        } catch (Exception e) {
            log.error("포인트 내역 페이지 로드 실패", e);
            model.addAttribute("pointHistory", Page.empty());
            model.addAttribute("error", "페이지 로드 중 오류가 발생했습니다: " + e.getMessage());
            return "member/point-history";
        }
    }
}