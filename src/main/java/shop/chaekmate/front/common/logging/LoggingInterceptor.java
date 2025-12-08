package shop.chaekmate.front.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String CLIENT_IP_MDC_KEY = "clientIp";
    private static final String REQUEST_METHOD_MDC_KEY = "requestMethod";
    private static final String REQUEST_URI_MDC_KEY = "requestUri";
    private static final String RESPONSE_STATUS_MDC_KEY = "responseStatus";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put(CLIENT_IP_MDC_KEY, getClientIp(request));
        MDC.put(REQUEST_METHOD_MDC_KEY, request.getMethod());
        MDC.put(REQUEST_URI_MDC_KEY, request.getRequestURI());

        log.info("[HTTP-IN] {} {}", request.getMethod(), request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.put(RESPONSE_STATUS_MDC_KEY, String.valueOf(response.getStatus()));

        log.info("[HTTP-OUT] {} {} status={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus()
        );

        // Clear MDC to prevent memory leaks and incorrect context in other threads
        MDC.remove(CLIENT_IP_MDC_KEY);
        MDC.remove(REQUEST_METHOD_MDC_KEY);
        MDC.remove(REQUEST_URI_MDC_KEY);
        MDC.remove(RESPONSE_STATUS_MDC_KEY);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
