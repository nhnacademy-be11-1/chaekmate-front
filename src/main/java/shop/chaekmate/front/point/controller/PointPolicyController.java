package shop.chaekmate.front.point.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.point.dto.request.PointPolicyUpdateRequest;
import shop.chaekmate.front.point.dto.response.PointPolicyResponse;
import shop.chaekmate.front.point.service.PointPolicyService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PointPolicyController {

    private final PointPolicyService pointPolicyService;

    @GetMapping("/admin/point-policy")
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

    @DeleteMapping("/admin/point-policy/{type}")
    public String deletePointPolicy(@PathVariable("type") String type) {
        pointPolicyService.deletePointPolicy(type);

        return "redirect:/admin/point-policy";
    }

    @PostMapping("/admin/point-policy")
    public String createPointPolicy(@RequestParam(required = false) String earnedType,
                                    @RequestParam(required = false) Integer point) {
        if (earnedType == null || earnedType.isEmpty()) {
            return "redirect:/admin/point-policy?error=타입을 선택해주세요";
        }
        if (point == null) {
            return "redirect:/admin/point-policy?error=포인트를 입력해주세요";
        }
        pointPolicyService.createPointPolicy(earnedType, point);

        return "redirect:/admin/point-policy";
    }

    @PostMapping("/admin/point-policy/{type}/update")
    public String updatePolicy(@PathVariable String type, @RequestParam int point) {
        pointPolicyService.updatePointPolicy(type, point);
        return "redirect:/admin/point-policy";
    }
}