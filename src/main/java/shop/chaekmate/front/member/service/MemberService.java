package shop.chaekmate.front.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.member.adaptor.MemberAdaptor;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;
import shop.chaekmate.front.member.dto.response.MemberAddressResponse;
import shop.chaekmate.front.member.dto.response.GradeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberAdaptor memberAdaptor;

    public void createMember(MemberCreateRequest memberCreateRequest){
        memberAdaptor.createMember(memberCreateRequest);
    }

    public List<MemberAddressResponse> getAddressesByMemberId(String memberId) {
        CommonResponse<List<MemberAddressResponse>> wrappedResponse = memberAdaptor.getAddresses(Long.valueOf(memberId));
        return wrappedResponse.data();
    }

    public GradeResponse getGradeByMemberId(String memberId) {
        CommonResponse<GradeResponse> wrappedResponse = memberAdaptor.getMemberGrade(Long.valueOf(memberId));
        return wrappedResponse.data();
    }

    public List<GradeResponse> getAllGrades() {
        CommonResponse<List<GradeResponse>> wrappedResponse = memberAdaptor.getAllGrades();
        return wrappedResponse.data();
    }
}
