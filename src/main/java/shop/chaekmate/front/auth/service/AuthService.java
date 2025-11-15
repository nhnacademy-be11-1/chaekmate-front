package shop.chaekmate.front.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.auth.adaptor.AuthAdaptor;
import shop.chaekmate.front.auth.dto.request.LoginRequest;
import shop.chaekmate.front.auth.dto.response.LoginResponse;
import shop.chaekmate.front.auth.dto.response.LogoutResponse;
import shop.chaekmate.front.auth.dto.response.MemberInfoResponse;
import shop.chaekmate.front.auth.util.CookieUtil;
import shop.chaekmate.front.auth.util.ResponseCookieUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthAdaptor authAdaptor;
    private final ResponseCookieUtil responseCookieUtil;

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        return authAdaptor.login(request);
    }

    public ResponseEntity<LoginResponse> adminLogin(LoginRequest request) {
        return authAdaptor.adminLogin(request);
    }

    public ResponseEntity<MemberInfoResponse> getMemberInfo(String token) {
        return authAdaptor.getMemberInfo(token);
    }

    public ResponseEntity<LogoutResponse> logout(String accessToken, String refreshToken) {
        return authAdaptor.logout(accessToken, refreshToken);
    }

    public ResponseEntity<LoginResponse> refreshToken(String refreshToken) {
        return authAdaptor.refreshToken(refreshToken);
    }

    // 회원 탈퇴 시 모든 토큰 제거되도록
    public void clearAllTokens(HttpServletRequest request, HttpServletResponse response) {
        // Auth 서버에서 RefreshToken 삭제
        String accessToken = CookieUtil.extractAccessTokenFromCookie(request);
        String refreshToken = CookieUtil.extractRefreshTokenFromCookie(request);

        if (accessToken != null || refreshToken != null) {
            try {
                authAdaptor.logout(accessToken, refreshToken);
            } catch (Exception e) {
                // 토큰이 만료되었거나 유효하지 않아도 계속 진행
            }
        }

        // front 서버에서 쿠키 삭제(accessToken, refreshToken)
        ResponseCookie deleteAccessTokenCookie = responseCookieUtil.createDeleteCookie("accessToken");
        ResponseCookie deleteRefreshTokenCookie = responseCookieUtil.createDeleteCookie("refreshToken");
        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie.toString());
    }
}
