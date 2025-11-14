package shop.chaekmate.front.member.dto.response;

public record MemberAddressResponse(
        Long id,
        String memo,
        String streetName,
        String detail,
        int zipcode
) { }