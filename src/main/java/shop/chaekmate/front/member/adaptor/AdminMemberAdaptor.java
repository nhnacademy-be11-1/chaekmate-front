package shop.chaekmate.front.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.member.dto.response.MemberResponse;

import java.util.List;

@FeignClient(name = "admin-member-adaptor", url = "${chaekmate.gateway.url}")
public interface AdminMemberAdaptor {

    @GetMapping("/admin/members")
    CommonResponse<List<MemberResponse>> getMembers(@RequestParam("status") String status);

    @GetMapping("/admin/members/{memberId}")
    CommonResponse<MemberResponse> getMember(@PathVariable Long memberId);

    @DeleteMapping("/admin/members/{memberId}")
    CommonResponse<Void> deleteMember(@PathVariable Long memberId);

    @PostMapping("/admin/members/{memberId}/restore")
    CommonResponse<Void> restoreMember(@PathVariable Long memberId);
}
