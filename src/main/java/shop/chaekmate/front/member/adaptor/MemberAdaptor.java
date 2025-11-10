package shop.chaekmate.front.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;
import shop.chaekmate.front.member.dto.response.MemberGradeResponse;

@FeignClient(name="member-client" , url = "${chaekmate.gateway.url}")
public interface MemberAdaptor {

    @PostMapping(value = "/members")
    CommonResponse<Void> createMember(@RequestBody MemberCreateRequest memberCreateRequest);

    @GetMapping(value = "/members/{memberId}/grades")
    CommonResponse<MemberGradeResponse> getGrade(@PathVariable String memberId);
}
