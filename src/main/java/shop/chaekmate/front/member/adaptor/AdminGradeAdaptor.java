package shop.chaekmate.front.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.member.dto.request.CreateGradeRequest;
import shop.chaekmate.front.member.dto.request.UpdateGradeRequest;

@FeignClient(name = "admin-grade-adaptor", url = "${chaekmate.gateway.url}")
public interface AdminGradeAdaptor {
    // 등급 수정
    @PutMapping("/admin/grades/{gradeId}")
    CommonResponse<Void> updateGrade(@PathVariable Long gradeId, @RequestBody UpdateGradeRequest request);

    // 등급 추가
    @PostMapping("/admin/grades")
    CommonResponse<Void> createGrade(@RequestBody CreateGradeRequest request);

    // 등급 삭제
    @DeleteMapping("/admin/grades/{gradeId}")
    CommonResponse<Void> deleteGrade(@PathVariable Long gradeId);
}
