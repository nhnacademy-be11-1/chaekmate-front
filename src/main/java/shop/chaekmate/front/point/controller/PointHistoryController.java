package shop.chaekmate.front.point.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.chaekmate.front.point.dto.response.PointHistoryResponse;
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
}