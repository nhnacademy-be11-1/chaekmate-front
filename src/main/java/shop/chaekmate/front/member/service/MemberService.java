package shop.chaekmate.front.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.member.adaptor.MemberAdaptor;
import shop.chaekmate.front.member.dto.request.MemberCreateRequest;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberAdaptor memberAdaptor;

    public void createMember(MemberCreateRequest memberCreateRequest){
        memberAdaptor.createMember(memberCreateRequest);
    }
}
