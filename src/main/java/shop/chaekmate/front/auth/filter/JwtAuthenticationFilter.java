package shop.chaekmate.front.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.chaekmate.front.auth.dto.response.MemberInfoResponse;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.auth.service.AuthService;
import shop.chaekmate.front.auth.util.CookieUtil;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = CookieUtil.extractAccessTokenFromCookie(request);

        if (accessToken != null) {
            try {
                ResponseEntity<MemberInfoResponse> memberInfoResponse = authService.getMemberInfo(accessToken);

                // HTTP 상태 코드가 200번대인지 체크
                if (memberInfoResponse.getStatusCode().is2xxSuccessful() &&
                        memberInfoResponse.getBody() != null)  {
                    MemberInfoResponse memberInfo = memberInfoResponse.getBody();

                    CustomPrincipal principal = new CustomPrincipal(
                            memberInfo.memberId(),
                            memberInfo.name(),
                            memberInfo.role());

                    // 권한 설정 (기본값: ROLE_USER)
                    String role = memberInfo.role();
                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    );

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
