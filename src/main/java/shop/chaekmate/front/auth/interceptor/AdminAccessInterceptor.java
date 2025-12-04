package shop.chaekmate.front.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Collection;

@Slf4j
@Component
public class AdminAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestPath = request.getRequestURI();

        // /admin 경로 체크 (모든 /admin 경로 포함)
        if (requestPath.startsWith("/admin")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증되지 않은 사용자는 통과 (로그인하지 않은 사용자는 관리자 로그인 페이지 접근 가능)
            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                return true;
            }

            // 회원(USER)이 관리자 페이지에 접근하려고 할 때 (로그인 페이지 포함)
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isUser = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

            if (isUser) {
                log.warn("회원이 관리자 페이지 접근 시도: path={}", requestPath);
                response.sendRedirect("/error/403");
                return false;
            }
        }

        return true;
    }
}

