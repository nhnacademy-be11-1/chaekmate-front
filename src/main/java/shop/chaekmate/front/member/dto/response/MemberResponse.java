package shop.chaekmate.front.member.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String loginId,
        String name,
        String phone,
        String email,
        LocalDate birthDate,
        String platformType,
        LocalDateTime lastLoginAt
) {}
