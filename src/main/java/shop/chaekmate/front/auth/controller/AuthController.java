package shop.chaekmate.front.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.auth.dto.request.LoginRequest;
import shop.chaekmate.front.auth.service.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false, name = "error") String errorParam, Model model) {
        model.addAttribute("title", "로그인 - Chaekmate");
        if (errorParam != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String  login(@RequestParam String loginId,
                         @RequestParam String password,
                         HttpServletResponse response) {
        try {
            LoginRequest request = new LoginRequest(loginId, password);
            ResponseEntity<?> gatewayResponse = authService.login(request);

            // Set-Cookie 헤더 추출하여 브라우저에 전달하려고
            if (gatewayResponse.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
                gatewayResponse.getHeaders().get(HttpHeaders.SET_COOKIE).forEach(cookieHeader ->
                        response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader)
                );
            }

            // 200대로 성공 여부 확인함
            if (gatewayResponse.getStatusCode().is2xxSuccessful()) {
                return "redirect:/";
            } else {
                return "redirect:/login?error=true";
            }
        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }
}
