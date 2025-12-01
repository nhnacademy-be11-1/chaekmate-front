package shop.chaekmate.front.cart.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GuestIdResolver {

    private static final String COOKIE_NAME = "Guest-Id";

    // Guest-Id 가져오기 또는 생성
    public String getOrCreateUuid(HttpServletRequest request, HttpServletResponse response) {
        String guestId = null;

        // 1. 요청 쿠키에서 UUID 확인
        if (Objects.nonNull(request.getCookies())) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    guestId = cookie.getValue();
                    break;
                }
            }
        }

        // 2. 없으면 새 UUID 생성 및 쿠키에 추가
        if (Objects.isNull(guestId)) {
            guestId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, guestId);
            cookie.setPath("/");                    // 모든 경로에서 접근 가능
            cookie.setHttpOnly(true);               // JS 접근 차단
            cookie.setMaxAge(60 * 60 * 24 * 30);    // 30일

            response.addCookie(cookie);
        }

        return guestId;
    }
}
