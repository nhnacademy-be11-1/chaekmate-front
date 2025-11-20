package shop.chaekmate.front.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.auth.dto.request.LoginRequest;
import shop.chaekmate.front.auth.dto.response.LoginResponse;
import shop.chaekmate.front.auth.dto.response.PaycoAuthorizationResponse;
import shop.chaekmate.front.auth.dto.response.PaycoTempInfoResponse;
import shop.chaekmate.front.auth.service.AuthService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private static final String REDIRECT_HOME = "redirect:/";
    private static final String REDIRECT_LOGIN_ERROR = "redirect:/login?error=true";
    private static final String REDIRECT_ADMIN_LOGIN_ERROR = "redirect:/admin/login?error=true";
    private static final String REDIRECT_ADMIN_HOME = "redirect:/admin";

    private final AuthService authService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false, name = "error") String errorParam, Model model) {
        model.addAttribute("title", "로그인 - Chaekmate");
        if (errorParam != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // PAYCO 인증 URL 가져오기
        try {
            ResponseEntity<PaycoAuthorizationResponse> paycoResponse = authService.getPaycoAuthorizationUrl();
            if (paycoResponse.getStatusCode().is2xxSuccessful() && paycoResponse.getBody() != null) {
                model.addAttribute("paycoAuthorizationUrl", paycoResponse.getBody().authorizationUrl());
            }
        } catch (Exception e) {
            // PAYCO URL 가져오기 실패 시 무시 (일반 로그인만 가능)
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId,
                         @RequestParam String password,
                         HttpServletResponse response) {
        try {
            LoginRequest request = new LoginRequest(loginId, password);
            ResponseEntity<LoginResponse> gatewayResponse = authService.login(request);

            // Set-Cookie 헤더 추출하여 브라우저에 전달하려고
            if (gatewayResponse.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
                List<String> cookieHeaders = gatewayResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
                if (cookieHeaders != null) {
                    cookieHeaders.forEach(cookieHeader -> response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader));
                }
            }

            // 200대로 성공 여부 확인함
            if (gatewayResponse.getStatusCode().is2xxSuccessful()) {
                return REDIRECT_HOME;
            } else {
                return REDIRECT_LOGIN_ERROR;
            }
        } catch (Exception e) {
            return REDIRECT_LOGIN_ERROR;
        }
    }

    @GetMapping("/admin/login")
    public String adminLogin(@RequestParam(required = false, name = "error") String errorParam, Model model) {
        model.addAttribute("title", "관리자 로그인 - Chaekmate");
        if (errorParam != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String loginId,
                             @RequestParam String password,
                             HttpServletResponse response) {
        try {
            LoginRequest request = new LoginRequest(loginId, password);
            ResponseEntity<LoginResponse> gatewayResponse = authService.adminLogin(request);

            // Set-Cookie 헤더 추출하여 브라우저에 전달하려고
            if (gatewayResponse.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
                List<String> cookieHeaders = gatewayResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
                if (cookieHeaders != null) {
                    cookieHeaders.forEach(cookieHeader -> response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader));
                }
            }

            // 200대로 성공 여부 확인함
            if (gatewayResponse.getStatusCode().is2xxSuccessful()) {
                return REDIRECT_ADMIN_HOME;
            } else {
                return REDIRECT_ADMIN_LOGIN_ERROR;
            }
        } catch (Exception e) {
            return REDIRECT_ADMIN_LOGIN_ERROR;
        }
    }

    @GetMapping("/auth/payco/callback")
    public String paycoCallback(@RequestParam("code") String code,
                                HttpServletResponse response) {
        try {
            ResponseEntity<PaycoTempInfoResponse> gatewayResponse = authService.paycoCallback(code);

            // 200대로 성공 여부 확인
            if (gatewayResponse.getStatusCode().is2xxSuccessful() && gatewayResponse.getBody() != null) {
                PaycoTempInfoResponse paycoInfo = gatewayResponse.getBody();

                // Set-Cookie 헤더 추출하여 브라우저에 전달
                if (gatewayResponse.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
                    List<String> cookieHeaders = gatewayResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
                    if (cookieHeaders != null) {
                        cookieHeaders.forEach(cookieHeader -> response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader));
                    }
                }

                // 기존 회원이면 홈으로, 신규 회원이면 PAYCO 회원가입 페이지로
                if (Boolean.TRUE.equals(paycoInfo.isExistingMember())) {
                    return REDIRECT_HOME;
                } else {
                    // tempKey를 쿼리 파라미터로 전달하여 PAYCO 회원가입 페이지로 리다이렉트
                    return "redirect:/signup/payco?tempKey=" + paycoInfo.tempKey();
                }
            } else {
                return REDIRECT_LOGIN_ERROR;
            }
        } catch (Exception e) {
            return REDIRECT_LOGIN_ERROR;
        }
    }

    @DeleteMapping("/auth/payco/temp/{tempKey}")
    public ResponseEntity<Void> deletePaycoTempInfo(@PathVariable String tempKey) {
        try {
            authService.deletePaycoTempInfo(tempKey);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/auth/payco/login")
    public ResponseEntity<LoginResponse> paycoAutoLogin(@RequestParam("paycoId") String paycoId,
                                                        HttpServletResponse response) {
        try {
            ResponseEntity<LoginResponse> gatewayResponse = authService.paycoAutoLogin(paycoId);

            // Set-Cookie 헤더 추출하여 브라우저에 전달
            if (gatewayResponse.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
                List<String> cookieHeaders = gatewayResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
                log.info("PAYCO 자동 로그인 - Set-Cookie 헤더 개수: {}", cookieHeaders != null ? cookieHeaders.size() : 0);
                if (cookieHeaders != null) {
                    cookieHeaders.forEach(cookieHeader -> {
                        log.info("PAYCO 자동 로그인 - Set-Cookie: {}", cookieHeader);
                        response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
                    });
                }
            } else {
                log.warn("PAYCO 자동 로그인 - Set-Cookie 헤더가 없습니다.");
            }

            return gatewayResponse;
        } catch (Exception e) {
            log.error("PAYCO 자동 로그인 실패", e);
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
