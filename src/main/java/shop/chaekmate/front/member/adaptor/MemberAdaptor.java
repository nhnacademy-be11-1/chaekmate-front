package shop.chaekmate.front.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.member.dto.request.AddressCreateRequest;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;
import shop.chaekmate.front.member.dto.response.MemberAddressResponse;
import shop.chaekmate.front.member.dto.response.GradeResponse;

import java.util.List;
import java.util.Map;

@FeignClient(name="member-client" , url = "${chaekmate.gateway.url}")
public interface MemberAdaptor {

    @PostMapping(value = "/members")
    CommonResponse<Void> createMember(@RequestBody MemberCreateRequest memberCreateRequest);

    @DeleteMapping(value = "/members/{memberId}")
    CommonResponse<Void> deleteMember(@PathVariable Long memberId);

    @GetMapping(value = "/members/check-login-id")
    CommonResponse<Map<String, Object>> checkLoginId(@RequestParam String loginId);

    @GetMapping(value = "/members/check-email")
    CommonResponse<Map<String, Object>> checkEmail(@RequestParam String email);

    @GetMapping(value = "/members/{memberId}/addresses")
    CommonResponse<List<MemberAddressResponse>> getAddresses(@PathVariable Long memberId);

    @PostMapping(value = "/members/{memberId}/addresses")
    CommonResponse<Void> createAddress(@PathVariable Long memberId, @RequestBody AddressCreateRequest addressCreateRequest);

    @DeleteMapping(value = "/members/{memberId}/addresses/{addressId}")
    CommonResponse<Void> deleteAddress(@PathVariable Long memberId, @PathVariable Long addressId);

    @GetMapping(value = "/members/{memberId}/grade")
    CommonResponse<GradeResponse> getMemberGrade(@PathVariable Long memberId);

    @GetMapping(value = "/members/grades")
    CommonResponse<List<GradeResponse>> getAllGrades();
}
