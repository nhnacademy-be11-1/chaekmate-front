package shop.chaekmate.front.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;
import shop.chaekmate.front.member.dto.response.MemberGradeResponse;

import java.util.Map;

@FeignClient(name="member-client" , url = "${chaekmate.gateway.url}")
public interface MemberAdaptor {

    @PostMapping(value = "/members")
    CommonResponse<Void> createMember(@RequestBody MemberCreateRequest memberCreateRequest);

    @GetMapping(value = "/members/check-login-id")
    CommonResponse<Map<String, Object>> checkLoginId(@RequestParam String loginId);

    @GetMapping(value = "/members/check-email")
    CommonResponse<Map<String, Object>> checkEmail(@RequestParam String email);

    @GetMapping(value = "/members/{memberId}/grades")
    CommonResponse<MemberGradeResponse> getGrade(@PathVariable String memberId);
}
