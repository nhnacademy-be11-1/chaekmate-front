package shop.chaekmate.front.auth.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class FeignCookieInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return;
        }

        // 모든 쿠키를 Cookie 헤더로 전달
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : cookies) {
            if (cookieHeader.isEmpty()) {
                cookieHeader.append("; ");
            }
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        }

        if (cookieHeader.isEmpty()) {
            requestTemplate.header("Cookie", cookieHeader.toString());
            log.debug("FeignCookieInterceptor 쿠키 자동 전달: {}", cookieHeader);
        }
    }
}
