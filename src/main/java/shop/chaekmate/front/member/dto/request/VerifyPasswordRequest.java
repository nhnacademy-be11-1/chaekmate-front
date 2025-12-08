package shop.chaekmate.front.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyPasswordRequest(
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 100, message = "비밀번호는 8자 이상, 100자 이하로 입력해주세요.")
        String password
) {
}
