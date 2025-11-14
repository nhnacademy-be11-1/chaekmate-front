package shop.chaekmate.front.auth.principal;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
     Spring Security Authentication의 Principal로 사용되는 사용자 정보 클래스입니다.
     JWT 인증 후 SecurityContext에 저장되며 Controller에서 @AuthenticationPrincipal로 주입받아
 */
@Getter
@AllArgsConstructor
public class CustomPrincipal implements Serializable {

    // 현재는 STATELESS 설정으로 세션에 저장하지 않아 직렬화가 발생하지 않지만 관례적으로 우선 유지
    @Serial
    private static final long serialVersionUID = 1L;

    private final Long memberId;
    private final String name;
    private final String role;
}