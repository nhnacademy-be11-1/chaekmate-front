package shop.chaekmate.front.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.auth.adaptor.AuthAdaptor;
import shop.chaekmate.front.auth.dto.request.LoginRequest;
import shop.chaekmate.front.auth.dto.response.LoginResponse;
import shop.chaekmate.front.auth.dto.response.LogoutResponse;
import shop.chaekmate.front.auth.dto.response.MemberInfoResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthAdaptor authAdaptor;

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        return authAdaptor.login(request);
    }

    public ResponseEntity<MemberInfoResponse> getMemberInfo(String token) {
        return authAdaptor.getMemberInfo(token);
    }

    public ResponseEntity<LogoutResponse> logout(String token) {
        return authAdaptor.logout(token);
    }
}
