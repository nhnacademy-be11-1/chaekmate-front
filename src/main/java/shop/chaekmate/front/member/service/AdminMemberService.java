package shop.chaekmate.front.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.member.adaptor.AdminMemberAdaptor;
import shop.chaekmate.front.member.dto.response.MemberResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final AdminMemberAdaptor adminMemberAdaptor;

    public List<MemberResponse> getActiveMembers() {
        return adminMemberAdaptor.getMembers("ACTIVE").data();
    }

    public List<MemberResponse> getDeletedMembers() {
        return adminMemberAdaptor.getMembers("DELETED").data();
    }

    public void deleteMember(Long memberId) {
        adminMemberAdaptor.deleteMember(memberId);
    }

    public void restoreMember(Long memberId) {
        adminMemberAdaptor.restoreMember(memberId);
    }
}
