package shop.chaekmate.front.point.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.point.dto.response.PointPolicyResponse;
import shop.chaekmate.front.point.service.PointPolicyService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PointPolicyController {

    private final PointPolicyService pointPolicyService;

    @GetMapping("/admin/point-policies")
    public String pointPolicyManagementView(Model model) {
        try {
            List<PointPolicyResponse> response = pointPolicyService.getAllPointPolicy();
            log.info("포인트 정책 데이터 개수: {}", response.size());

            // PointEarnedType enum 값들과 한글 표시명 매핑
            Map<String, String> earnedTypeMap = new LinkedHashMap<>();
            earnedTypeMap.put("WELCOME", "회원가입");
            earnedTypeMap.put("ORDER", "주문");
            earnedTypeMap.put("REVIEW", "리뷰작성");

            model.addAttribute("pointPolicy", response);
            model.addAttribute("earnedTypeMap", earnedTypeMap);

            return "admin/point/point-management";
        } catch (Exception e) {
            log.error("포인트 정책 페이지 로드 실패", e);
            model.addAttribute("error", "페이지 로드 중 오류가 발생했습니다: " + e.getMessage());
            return "admin/point/point-management";
        }
    }

    @PostMapping("/admin/point-policies/{type}/delete")
    public String deletePointPolicy(@PathVariable("type") String type, RedirectAttributes redirectAttributes) {
        try {
            pointPolicyService.deletePointPolicy(type);
            log.info("===== Controller: 리다이렉트 실행 =====");
            return "redirect:/admin/point-policies";
        } catch (RuntimeException e) {
            log.error("포인트 정책 삭제 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/point-policies";
        }
    }

    @PostMapping("/admin/point-policies")
    public String createPointPolicy(@RequestParam(required = false) String earnedType,
                                    @RequestParam(required = false) Integer point,
                                    RedirectAttributes redirectAttributes) {
        if (earnedType == null || earnedType.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "타입을 선택해주세요");
            return "redirect:/admin/point-policies";
        }
        if (point == null) {
            redirectAttributes.addFlashAttribute("error", "포인트를 입력해주세요");
            return "redirect:/admin/point-policies";
        }

        try {
            pointPolicyService.createPointPolicy(earnedType, point);
            log.info("===== 포인트 정책 생성 성공 =====");
            return "redirect:/admin/point-policies";
        } catch (RuntimeException e) {
            log.error("포인트 정책 생성 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/point-policies";
        }
    }


    @PostMapping("/admin/point-policies/{type}/update")
    public String updatePolicy(@PathVariable String type,
                               @RequestParam int point,
                               RedirectAttributes redirectAttributes) {
        log.info("===== POST /admin/point-policies/{}/update 요청: point={} =====", type, point);

        try {
            pointPolicyService.updatePointPolicy(type, point);
            log.info("===== 포인트 정책 수정 성공 =====");
            return "redirect:/admin/point-policies";
        } catch (RuntimeException e) {
            log.error("포인트 정책 수정 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/point-policies";
        }
    }
}