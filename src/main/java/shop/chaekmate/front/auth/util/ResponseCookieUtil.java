package shop.chaekmate.front.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import shop.chaekmate.front.auth.config.CookieConfig;

@Component
@RequiredArgsConstructor
public class ResponseCookieUtil {

    private final CookieConfig cookieConfig;

    // 쿠키 삭제하기 위한 쿠키
    public ResponseCookie createDeleteCookie(String cookieName) {
        return ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(cookieConfig.isSecureCookie())
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}