package shop.chaekmate.front.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddressCreateRequest(
        String memo,
        @NotBlank(message = "도로명주소를 입력해야 합니다.")
        String streetName,
        @NotBlank(message = "상세 주소를 입력하세요.")
        String detail,
        @Positive(message = "유효한 우편번호를 입력하세요.")
        int zipcode
) {}