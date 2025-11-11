package shop.chaekmate.front.auth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import shop.chaekmate.front.auth.config.CookieConfig;
import shop.chaekmate.front.auth.service.AuthService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final AuthService authService;
    private final CookieConfig cookieConfig;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        // Auth Server에 logout 요청 (Redis에서 RefreshToken 삭제)
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            try {
                authService.logout(accessToken);
            } catch (Exception e) {
                log.warn("로그아웃 중 Auth Server 호출 실패: {}", e.getMessage());
            }
        }

        // Front Server에서 accessToken 쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(cookieConfig.isSecureCookie())
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    }
}