package shop.chaekmate.front.auth.dto.response;

public record MemberInfoResponse(
        Long memberId,
        String name,
        String role
) {
}
