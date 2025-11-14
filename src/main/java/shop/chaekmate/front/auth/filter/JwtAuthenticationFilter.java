package shop.chaekmate.front.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.chaekmate.front.auth.dto.response.LoginResponse;
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
                // 여기서 만료된 토큰이니까 이제 예외 발생될거임.
                ResponseEntity<MemberInfoResponse> memberInfoResponse = authService.getMemberInfo(accessToken);

                // HTTP 상태 코드가 200번대인지 체크
                if (memberInfoResponse.getStatusCode().is2xxSuccessful() &&
                        memberInfoResponse.getBody() != null) {
                    setAuthentication(memberInfoResponse.getBody());
                }
            } catch (Exception e) {
                // AccessToken 검증 실패 시 RefreshToken으로 재발급 시도
                handleTokenRefresh(request, response);
            }
        } else {
            // AccessToken이 없으면 (만료되어 쿠키에서 사라진 경우) RefreshToken으로 재발급 시도
            handleTokenRefresh(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(MemberInfoResponse memberInfo) {
        CustomPrincipal principal = new CustomPrincipal(
                memberInfo.memberId(),
                memberInfo.name(),
                memberInfo.role());

        // 권한 설정
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

    private void handleTokenRefresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            SecurityContextHolder.clearContext();
            return;
        }

        try {
            ResponseEntity<LoginResponse> refreshResponse = authService.refreshToken(refreshToken);

            if (refreshResponse.getStatusCode().is2xxSuccessful()) {
                String newAccessToken = extractAndSetCookies(refreshResponse, response);

                if (newAccessToken != null) {
                    authenticateWithNewToken(newAccessToken);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
    }

    private String extractAndSetCookies(ResponseEntity<LoginResponse> refreshResponse, HttpServletResponse response) {
        if (!refreshResponse.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
            return null;
        }

        List<String> cookieHeaders = refreshResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (cookieHeaders == null) {
            return null;
        }

        String newAccessToken = null;
        for (String cookieHeader : cookieHeaders) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);

            if (cookieHeader.startsWith("accessToken=")) {
                String[] parts = cookieHeader.split(";")[0].split("=", 2);
                if (parts.length == 2) {
                    newAccessToken = parts[1];
                }
            }
        }

        return newAccessToken;
    }

    private void authenticateWithNewToken(String accessToken) {
        try {
            ResponseEntity<MemberInfoResponse> memberInfoResponse = authService.getMemberInfo(accessToken);

            if (memberInfoResponse.getStatusCode().is2xxSuccessful() &&
                    memberInfoResponse.getBody() != null) {
                setAuthentication(memberInfoResponse.getBody());
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
    }
}
