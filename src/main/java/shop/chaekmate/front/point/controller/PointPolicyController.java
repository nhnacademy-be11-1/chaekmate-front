package shop.chaekmate.front.point.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.point.dto.request.PointPolicyUpdateRequest;
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
        List<PointPolicyResponse> response = pointPolicyService.getAllPointPolicy();

        // PointEarnedType enum 값들과 한글 표시명 매핑
        Map<String, String> earnedTypeMap = new LinkedHashMap<>();
        earnedTypeMap.put("WELCOME", "회원가입");
        earnedTypeMap.put("ORDER", "주문");
        earnedTypeMap.put("REVIEW", "리뷰작성");

        model.addAttribute("pointPolicy", response);
        model.addAttribute("earnedTypeMap", earnedTypeMap);
        return "admin/point/point-management";
    }

    @PostMapping("/admin/point-policies/{type}/delete")
    public String deletePointPolicy(@PathVariable("type") String type) {
        log.info("===== Controller: 삭제 요청 받음 - type: {} =====", type);
        pointPolicyService.deletePointPolicy(type);
        log.info("===== Controller: 리다이렉트 실행 =====");
        return "redirect:/admin/point-policies";
    }

    @PostMapping("/admin/point-policies")
    public String createPointPolicy(@RequestParam(required = false) String earnedType,
                                    @RequestParam(required = false) Integer point) {
        if (earnedType == null || earnedType.isEmpty()) {
            return "redirect:/admin/point-policies?error=타입을 선택해주세요";
        }
        if (point == null) {
            return "redirect:/admin/point-policies?error=포인트를 입력해주세요";
        }
        pointPolicyService.createPointPolicy(earnedType, point);

        return "redirect:/admin/point-policies";
    }

    @PutMapping("/admin/point-policies/{type}/update")
    public String updatePolicy(@PathVariable String type, @RequestParam int point) {
        pointPolicyService.updatePointPolicy(type, point);
        return "redirect:/admin/point-policies";
    }
}