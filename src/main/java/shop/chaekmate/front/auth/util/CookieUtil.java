package shop.chaekmate.front.auth.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    // HttpServletRequest에서 accessToken을 추출함
    public static String extractAccessTokenFromCookie(HttpServletRequest request) {
        if(request == null) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }

        for(Cookie cookie : cookies) {
            if(ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }
}
